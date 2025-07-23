package org.core.ged.payload.response;

public record TagResponse(
    String id,
    String name,
    String createdAt,
    String updatedAt
) {
    
}
