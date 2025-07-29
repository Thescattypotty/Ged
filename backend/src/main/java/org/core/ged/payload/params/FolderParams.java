package org.core.ged.payload.params;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestParam;

public record FolderParams(
    @RequestParam(name = "query", required = false)
    String query,
    @RequestParam(name = "parentsId", required = false)
    List<UUID> parentsId
) {
    
}
