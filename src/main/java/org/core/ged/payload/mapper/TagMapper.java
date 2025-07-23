package org.core.ged.payload.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.core.ged.entity.Tag;
import org.core.ged.payload.request.TagRequest;
import org.core.ged.payload.response.TagResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagMapper {
    
    public static Tag toTag(TagRequest request){
        return Tag.builder()
            .name(request.name())
            .build();
    }

    public static TagResponse fromTag(Tag tag){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new TagResponse(
            tag.getId().toString(),
            tag.getName(),
            tag.getCreatedAt().format(formatter),
            tag.getUpdatedAt().format(formatter)
        );
    }

    public static List<TagResponse> fromTags(List<Tag> tags) {
        return tags.stream()
            .map(TagMapper::fromTag)
            .toList();
    }

}
