package org.core.ged.service;

import java.util.UUID;

import org.core.ged.entity.Folder;
import org.core.ged.exception.TechnicalException;
import org.core.ged.iservice.IFolderService;
import org.core.ged.payload.mapper.FolderMapper;
import org.core.ged.payload.params.FolderParams;
import org.core.ged.payload.request.FolderRequest;
import org.core.ged.payload.response.FolderResponse;
import org.core.ged.repository.FolderRepository;
import org.core.ged.repository.MinioRepository;
import org.core.ged.specification.FolderSpecification;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FolderService implements IFolderService{
    
    private final FolderRepository folderRepository;
    private final MinioRepository minioRepository;

    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        readOnly = false,
        rollbackFor = {TechnicalException.class}
    )
    public void createFolder(FolderRequest request) {
        String path = "/";
        Folder parent = null;
        if(request.parentId() != null && folderRepository.existsById(UUID.fromString(request.parentId()))){
            log.info("Parent folder found with ID: {}", request.parentId());
            parent = folderRepository.findById(UUID.fromString(request.parentId()))
                .orElseThrow(() -> new TechnicalException("Parent folder not found", HttpStatus.NOT_FOUND));
            log.info("Parent folder path: {}", parent.getPath());
            path = parent.getPath();
        }
        try {
            log.info("Creating folder in Minio with path: {}", path);
            path = minioRepository.createFolder(path, request.name());
        } catch (Exception e) {
            log.error("Failed to create folder in Minio: {}", e.getMessage(), e);
            throw new TechnicalException("Failed to create folder in Minio", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Folder folder = FolderMapper.toFolder(request);
        folder.setPath(path);
        folder.setParent(parent);

        folderRepository.save(folder);
        log.info("Folder created successfully with ID: {}", folder.getId());
    }

    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        timeout = 10,
        readOnly = false,
        rollbackFor = {TechnicalException.class}
    )
    public void deleteFolder(String id) {
        if (!folderRepository.existsById(UUID.fromString(id))) {
            log.error("Folder with ID {} not found", id);
            throw new TechnicalException("Folder not found", HttpStatus.NOT_FOUND);
        }
        try {
            log.info("Deleting folder in Minio with ID: {}", id);
            Folder folder = folderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new TechnicalException("Folder not found", HttpStatus.NOT_FOUND));
            minioRepository.deleteFolder(folder.getPath());
            log.info("Folder deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete folder in Minio: {}", e.getMessage(), e);
            throw new TechnicalException("Failed to delete folder in Minio", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        folderRepository.deleteById(UUID.fromString(id));
    }

    @Override
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation = Isolation.READ_COMMITTED,
        timeout = 10,
        readOnly = true
    )
    public FolderResponse getFolder(String id) {
        Folder folder = folderRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new TechnicalException("Folder not found", HttpStatus.NOT_FOUND));
        log.info("Retrieved folder with ID: {}", id);
        return FolderMapper.fromFolder(folder);
    }

    @Override
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation = Isolation.READ_COMMITTED,
        timeout = 10,
        readOnly = true
    )
    public Page<FolderResponse> getAllFolders(int page, int size, String sortBy, String sortDir, FolderParams params) {
        Sort sort = Sort.by(Direction.fromString(sortDir), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        log.debug("Retrieving tags with params: page={}, size={}, sortBy={}, sortDir={}, params={}", page, size, sortBy, sortDir, params);

        Specification<Folder> specification = Specification.anyOf();
        if(params != null){
            specification = FolderSpecification.search(params);
        }
        Page<Folder> folderPage = folderRepository.findAll(specification, pageRequest);
        log.info("Retrieved {} folders", folderPage.getTotalElements());
        return folderPage.map(FolderMapper::fromFolder);
    }    
}
