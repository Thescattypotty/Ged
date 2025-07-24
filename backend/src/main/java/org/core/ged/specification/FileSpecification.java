package org.core.ged.specification;

import org.core.ged.entity.File;
import org.core.ged.payload.params.FileParams;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileSpecification {
    
    public static Specification<File> search(FileParams params){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicates = criteriaBuilder.conjunction();
            
            if(params.query() != null && !params.query().isBlank()){
                String query = "%" + params.query().toLowerCase() + "%";
                predicates = criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), query));
            }

            if(params.folderIds() != null && !params.folderIds().isEmpty()){
                predicates = criteriaBuilder.and(predicates, root.get("folder").get("id").in(params.folderIds()));
            }

            if(params.tagsIds() != null && !params.tagsIds().isEmpty()){
                predicates = criteriaBuilder.and(predicates, root.join("tags").get("id").in(params.tagsIds()));
            }

            return predicates;
        };
    }
}
