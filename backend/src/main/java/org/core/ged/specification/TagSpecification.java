package org.core.ged.specification;


import org.core.ged.entity.Tag;
import org.core.ged.payload.params.TagParams;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagSpecification {

    public static Specification<Tag> search(TagParams params){
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicates = criteriaBuilder.conjunction();

            if(params.query() != null && !params.query().isBlank()) {
                String query = "%" + params.query().toLowerCase() + "%";
                predicates = criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), query));
            }
            
            return predicates;
        };
    }
}
