package org.core.ged.controller;

import org.core.ged.iservice.IFileService;
import org.core.ged.payload.dto.FileDto;
import org.core.ged.payload.params.FileParams;
import org.core.ged.payload.request.FileRequest;
import org.core.ged.payload.response.FileResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final IFileService fileService;

    @PostMapping
    public ResponseEntity<Void> createFile(
        @RequestPart(name = "file", required = true) MultipartFile file,
        @RequestPart(name = "request", required = true) FileRequest request
        ) {
        log.info("Creating file with name: {}", request.name());
        fileService.createFile(file, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable("id") String id) {
        log.info("Deleting file with id: {}", id);
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> getFile(@PathVariable("id") String id) {
        log.info("Retrieving file with id: {}", id);
        FileResponse fileResponse = fileService.getFile(id);
        return new ResponseEntity<>(fileResponse, HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<FileDto> downloadFile(@PathVariable("id") String id) {
        log.info("Downloading file with id: {}", id);
        FileDto fileDto = fileService.downloadFile(id);
        return new ResponseEntity<>(fileDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<FileResponse>> getFiles(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
        FileParams params
    ) {
        log.info("Retrieving files with params: page={}, size={}, sortBy={}, sortDir={}, params={}", page, size, sortBy, sortDir, params);
        Page<FileResponse> fileResponses = fileService.getFiles(page, size, sortBy, sortDir, params);
        return new ResponseEntity<>(fileResponses, HttpStatus.OK);
    }



}
