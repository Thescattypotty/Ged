package org.core.ged.service;

import java.io.InputStream;
import java.util.UUID;

import org.core.ged.entity.File;
import org.core.ged.entity.Folder;
import org.core.ged.exception.TechnicalException;
import org.core.ged.iservice.IFileService;
import org.core.ged.payload.dto.FileDto;
import org.core.ged.payload.mapper.FileMapper;
import org.core.ged.payload.params.FileParams;
import org.core.ged.payload.request.FileRequest;
import org.core.ged.payload.response.FileResponse;
import org.core.ged.repository.FileRepository;
import org.core.ged.repository.FolderRepository;
import org.core.ged.repository.MinioRepository;
import org.core.ged.repository.TagRepository;
import org.core.ged.specification.FileSpecification;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService{

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final MinioRepository minioRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        readOnly = false,
        rollbackFor = {TechnicalException.class}
    )
    public void createFile(MultipartFile multipartFile, FileRequest request) {
        log.info("Creating file with name: {}", request.name());
        Folder folder = folderRepository.findById(UUID.fromString(request.folderId()))
            .orElseThrow(() -> new TechnicalException("Folder not found", HttpStatus.NOT_FOUND));

        if(fileRepository.existsByNameAndFolder(request.name(), folder)){
            log.error("File with name {} already exists in folder {}", request.name(), folder.getName());
            throw new TechnicalException("File with this name already exists in the specified folder", HttpStatus.CONFLICT);
        }
        String idRef = null;
        String extension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf('.') + 1);
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String mimeType = multipartFile.getContentType();
            idRef = minioRepository.createFile(folder.getPath(), request.name() + "." + extension, inputStream, mimeType);
        } catch (Exception e) {
            log.error("Error while creating file: {}", e.getMessage());
            throw new TechnicalException("Error while creating file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        File file = FileMapper.toFile(request);
        file.setFolder(folder);
        file.setType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        file.setExtension(extension);
        file.setIdReference(idRef);

        file.setTags(tagRepository.findAllById(request.tagsId().stream().map(UUID::fromString).toList()));
        fileRepository.save(file);
        log.info("File with name {} created successfully in folder {}", request.name(), folder.getName());
    }

    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        timeout = 10,
        readOnly = false,
        rollbackFor = {TechnicalException.class}
    )
    public void deleteFile(String id) {
        File file = fileRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new TechnicalException("File not found", HttpStatus.NOT_FOUND));
        try {
            minioRepository.deleteFile(file.getIdReference());
        } catch (Exception e) {
            log.error("Error while deleting file: {}", e.getMessage());
            throw new TechnicalException("Error while deleting file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        fileRepository.deleteById(UUID.fromString(id));
        log.info("File with id {} deleted successfully", id);
    }

    @Override
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        readOnly = true
    )
    public FileResponse getFile(String id) {
        File file = fileRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new TechnicalException("File not found", HttpStatus.NOT_FOUND));
        log.info("Retrieving file with id: {}", id);
        return FileMapper.fromFile(file);
    }

    @Override
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        readOnly = true
    )
    public FileDto downloadFile(String id) {
        File file = fileRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new TechnicalException("File not found", HttpStatus.NOT_FOUND));
        log.info("Downloading file with id: {}", id);
        try {
            Resource resource = minioRepository.getFile(file.getIdReference());
            return new FileDto(file.getName() + "." + file.getExtension(), file.getType(), resource.getContentAsByteArray());
        } catch (Exception e) {
            log.error("Error while downloading file: {}", e.getMessage());
            throw new TechnicalException("Error while downloading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        readOnly = true
    )
    public Page<FileResponse> getFiles(int page, int size, String sortBy, String sortDir, FileParams params) {
        Sort sort = Sort.by(Direction.fromString(sortDir), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        log.debug("Retrieving tags with params: page={}, size={}, sortBy={}, sortDir={}, params={}", 
                  page, size, sortBy, sortDir, params);
        Specification<File> specification = Specification.anyOf();
        if (params != null) {
            specification = FileSpecification.search(params);
        }
        Page<File> files = fileRepository.findAll(specification, pageRequest);
        log.info("Retrieved {} files", files.getTotalElements());
        return files.map(FileMapper::fromFile);
    }
    
}
