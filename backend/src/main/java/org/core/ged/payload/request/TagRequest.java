package org.core.ged.payload.request;

import org.core.ged.annotation.UniqueTag;

import jakarta.validation.constraints.NotBlank;

public record TagRequest(
    @NotBlank(message = "Tag name cannot be blank")
    @UniqueTag
    String name
) {
    
}
