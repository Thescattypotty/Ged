package org.core.ged.payload.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record FileRequest(
    @NotBlank(message = "Name cannot be blank")
    String name,
    @NotBlank(message = "Folder cannot be blank")
    String folderId,
    List<String> tagsId
) {
    
}
