package org.core.ged.payload.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.core.ged.entity.File;
import org.core.ged.payload.request.FileRequest;
import org.core.ged.payload.response.FileResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileMapper {
    public static File toFile(FileRequest request){
        return File.builder()
            .name(request.name())
            .build();
    }

    public static FileResponse fromFile(File file){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new FileResponse(
            file.getId().toString(),
            file.getName(),
            file.getExtension(),
            file.getType(),
            file.getSize(),
            file.getFolder() != null ? file.getFolder().getId().toString() : null,
            file.getTags() != null ? file.getTags().stream().map(tag -> tag.getName()).toList() : null,
            file.getIdReference(),
            file.getCreatedAt().format(formatter),
            file.getUpdatedAt().format(formatter)
        );
    }

    public static List<FileResponse> fromFiles(List<File> files) {
        return files.stream()
            .map(FileMapper::fromFile)
            .toList();
    }
}
