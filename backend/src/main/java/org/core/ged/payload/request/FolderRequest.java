package org.core.ged.payload.request;

import jakarta.validation.constraints.NotBlank;

public record FolderRequest(
    @NotBlank(message = "Name cannot be blank")
    String name,

    String description,

    String parentId
) {
    
}
