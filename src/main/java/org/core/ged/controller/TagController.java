package org.core.ged.controller;

import org.core.ged.iservice.ITagService;
import org.core.ged.payload.params.TagParams;
import org.core.ged.payload.request.TagRequest;
import org.core.ged.payload.response.TagResponse;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
@Slf4j
public class TagController {
    private final ITagService tagService;

    @PostMapping
    public ResponseEntity<Void> createTag(@RequestBody @Valid TagRequest request) {
        log.info("Creating tag with request: {}", request);
        tagService.createTag(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable String id) {
        log.info("Deleting tag with id: {}", id);
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTag(@PathVariable String id) {
        log.info("Retrieving tag with id: {}", id);
        TagResponse tagResponse = tagService.getTag(id);
        return new ResponseEntity<>(tagResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<TagResponse>> getTags(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir,
        TagParams params
        
    ){
        log.info("Retrieving tags with params: page={}, size={}, sortBy={}, sortDir={}, params={}", page, size, sortBy, sortDir, params);
        Page<TagResponse> tagResponses = tagService.getTags(page, size, sortBy, sortDir, params);
        return new ResponseEntity<>(tagResponses, HttpStatus.OK);
    }
}
