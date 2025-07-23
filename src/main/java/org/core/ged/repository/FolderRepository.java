package org.core.ged.repository;

import java.util.UUID;

import org.core.ged.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID>, JpaSpecificationExecutor<Folder>{
    Optional<Folder> findByName(String name);
}
