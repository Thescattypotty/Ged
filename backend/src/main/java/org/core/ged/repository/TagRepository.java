package org.core.ged.repository;

import java.util.UUID;

import org.core.ged.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID>, JpaSpecificationExecutor<Tag>{
    boolean existsByNameIgnoreCase(String name);
}
