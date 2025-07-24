package org.core.ged.iservice;

import org.core.ged.payload.dto.FileDto;
import org.core.ged.payload.params.FileParams;
import org.core.ged.payload.request.FileRequest;
import org.core.ged.payload.response.FileResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    void createFile(MultipartFile file, FileRequest request);
    void deleteFile(String id);

    FileResponse getFile(String id);
    FileDto downloadFile(String id);

    Page<FileResponse> getFiles(int page, int size, String sortBy, String sortDir, FileParams params);
}
