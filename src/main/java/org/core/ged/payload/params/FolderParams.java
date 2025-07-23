package org.core.ged.payload.params;

import java.util.List;

public record FolderParams(
    String query,
    List<String> parentsId
) {
    
}
