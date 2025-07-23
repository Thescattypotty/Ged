package org.core.ged.payload.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.core.ged.entity.Folder;
import org.core.ged.payload.request.FolderRequest;
import org.core.ged.payload.response.FolderResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FolderMapper {
    
    public static Folder toFolder(FolderRequest request){
        return Folder.builder()
            .name(request.name())
            .description(request.description())
            .build();
    }

    public static FolderResponse fromFolder(Folder folder){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new FolderResponse(
            folder.getId().toString(),
            folder.getName(),
            folder.getParent() != null ? folder.getParent().getId().toString() : null,
            folder.getDescription(),
            folder.getPath(),
            folder.getCreatedAt().format(formatter),
            folder.getUpdatedAt().format(formatter)
        );
    }

    public static List<FolderResponse> fromFolders(List<Folder> folders) {
        return folders.stream()
            .map(FolderMapper::fromFolder)
            .toList();
    }
}
