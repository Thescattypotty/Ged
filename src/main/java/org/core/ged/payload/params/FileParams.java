package org.core.ged.payload.params;

import java.util.List;

public record FileParams(
    String query,
    List<String> folderIds,
    List<String> tagsIds
) {
    
}
