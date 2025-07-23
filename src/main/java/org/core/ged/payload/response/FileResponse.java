package org.core.ged.payload.response;

import java.util.List;

public record FileResponse(
    String id,
    String name,
    String extension,
    String type,
    Long size,
    String folderId,
    List<String> tags,
    String idReference,
    String createdAt,
    String updatedAt
) {
    
}
