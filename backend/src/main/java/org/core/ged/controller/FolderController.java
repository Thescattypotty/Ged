package org.core.ged.controller;

import org.core.ged.iservice.IFolderService;
import org.core.ged.payload.params.FolderParams;
import org.core.ged.payload.request.FolderRequest;
import org.core.ged.payload.response.FolderResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
@Slf4j
public class FolderController {
    private final IFolderService folderService;

    @PostMapping
    public ResponseEntity<Void> createFolder(@RequestBody @Valid FolderRequest request) {
        log.info("Creating folder with request: {}", request);
        folderService.createFolder(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable("id") String id) {
        log.info("Deleting folder with id: {}", id);
        folderService.deleteFolder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderResponse> getFolder(@PathVariable("id") String id) {
        log.info("Retrieving folder with id: {}", id);
        FolderResponse folder = folderService.getFolder(id);
        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<FolderResponse>> getAllFolders(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
        FolderParams params
    ) {
        log.info("Retrieving all folders with params: {}, page: {}, size: {}, sortBy: {}, sortDir: {}",
                params, page, size, sortBy, sortDir);
        Page<FolderResponse> folders = folderService.getAllFolders(page, size, sortBy, sortDir, params);
        return new ResponseEntity<>(folders, HttpStatus.OK);
    }
}
