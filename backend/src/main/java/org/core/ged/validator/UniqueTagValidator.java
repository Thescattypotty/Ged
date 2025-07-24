package org.core.ged.validator;

import org.core.ged.annotation.UniqueTag;
import org.core.ged.repository.TagRepository;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UniqueTagValidator implements ConstraintValidator<UniqueTag, String>{
    
    private final TagRepository tagRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isBlank()) return true;
        return !tagRepository.existsByNameIgnoreCase(value);
    }
    
}
