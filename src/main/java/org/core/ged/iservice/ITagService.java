package org.core.ged.iservice;

import org.core.ged.payload.params.TagParams;
import org.core.ged.payload.request.TagRequest;
import org.core.ged.payload.response.TagResponse;
import org.springframework.data.domain.Page;

public interface ITagService {
    void createTag(TagRequest request);
    void deleteTag(String id);
    
    TagResponse getTag(String id);
    Page<TagResponse> getTags(int page, int size, String sortBy, String sortDir, TagParams params);
}
