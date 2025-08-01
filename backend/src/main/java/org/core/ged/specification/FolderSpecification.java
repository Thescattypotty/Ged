package org.core.ged.specification;

import org.core.ged.entity.Folder;
import org.core.ged.payload.params.FolderParams;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FolderSpecification {
    
    public static Specification<Folder> search(FolderParams params){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicates = criteriaBuilder.conjunction();

            if(params.query() != null && !params.query().isBlank()) {
                String query = "%" + params.query().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), query);
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), query);
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.or(namePredicate, descriptionPredicate));
            }

            if(params.parentsId() != null && !params.parentsId().isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.get("parent").get("id").in(params.parentsId()));
            }

            return predicates;
        };
    }
}
