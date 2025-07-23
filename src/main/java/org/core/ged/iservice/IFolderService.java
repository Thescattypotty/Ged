package org.core.ged.iservice;

import org.core.ged.payload.params.FolderParams;
import org.core.ged.payload.request.FolderRequest;
import org.core.ged.payload.response.FolderResponse;
import org.springframework.data.domain.Page;

public interface IFolderService {
    void createFolder(FolderRequest request);
    void deleteFolder(String id);

    FolderResponse getFolder(String id);
    Page<FolderResponse> getAllFolders(int page, int size, String sortBy, String sortDir, FolderParams params);
}
