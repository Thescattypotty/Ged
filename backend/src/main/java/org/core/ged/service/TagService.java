package org.core.ged.service;

import java.util.UUID;

import org.core.ged.entity.Tag;
import org.core.ged.exception.TechnicalException;
import org.core.ged.iservice.ITagService;
import org.core.ged.payload.mapper.TagMapper;
import org.core.ged.payload.params.TagParams;
import org.core.ged.payload.request.TagRequest;
import org.core.ged.payload.response.TagResponse;
import org.core.ged.repository.TagRepository;
import org.core.ged.specification.TagSpecification;
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
public class TagService implements ITagService{
    
    private final TagRepository tagRepository;

    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        readOnly = false,
        rollbackFor = {TechnicalException.class}
    )
    public void createTag(TagRequest request) {
        Tag tag = TagMapper.toTag(request);
        log.debug("Creating tag: {}", tag);
        tagRepository.save(tag);
        log.info("Tag created successfully: {}", tag.getId());
    }

    @Override
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        timeout = 10,
        readOnly = false,
        rollbackFor = {TechnicalException.class}
    )
    public void deleteTag(String id) {
        log.debug("Deleting tag with id: {}", id);
        Tag tag = tagRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new TechnicalException("Tag not found", HttpStatus.NOT_FOUND));
        tagRepository.delete(tag);
        log.info("Tag deleted successfully: {}", id);
    }

    @Override
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        readOnly = true
    )
    public TagResponse getTag(String id) {
        Tag tag = tagRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new TechnicalException("Tag not found", HttpStatus.NOT_FOUND));
        log.debug("Retrieving tag with id: {}", id);
        return TagMapper.fromTag(tag);
    }

    @Override
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation = Isolation.READ_COMMITTED,
        timeout = 30,
        readOnly = true
    )
    public Page<TagResponse> getTags(int page, int size, String sortBy, String sortDir, TagParams params) {
        Sort sort = Sort.by(Direction.fromString(sortDir), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        log.debug("Retrieving tags with params: page={}, size={}, sortBy={}, sortDir={}, params={}", 
                  page, size, sortBy, sortDir, params);
        Specification<Tag> specification = Specification.anyOf();

        if(params != null){
            specification =  TagSpecification.search(params);
        }

        Page<Tag> tagPage = tagRepository.findAll(specification, pageRequest);
        log.info("Retrieved {} tags", tagPage.getTotalElements());
        return tagPage.map(TagMapper::fromTag);
    }
    
}
