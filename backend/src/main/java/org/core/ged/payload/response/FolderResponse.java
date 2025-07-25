package org.core.ged.payload.response;

public record FolderResponse(
    String id,
    String name,
    FolderResponse parent,
    String description,
    String path,
    String createdAt,
    String updatedAt
) {
    
}
