package org.core.ged.payload.dto;

public record FileDto(
    String fileName,
    String mimeType,
    byte[] content
) {
    
}
