package org.core.ged.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.core.ged.validator.UniqueTagValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = UniqueTagValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTag {
    String message() default "Tag name must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
