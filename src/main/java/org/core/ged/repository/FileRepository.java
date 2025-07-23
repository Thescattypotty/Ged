package org.core.ged.repository;

import java.util.UUID;

import org.core.ged.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface FileRepository extends JpaRepository<File, UUID>, JpaSpecificationExecutor<File>{
    Optional<File> findByName(String name);
}
