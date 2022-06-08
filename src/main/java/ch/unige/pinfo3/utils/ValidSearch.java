package ch.unige.pinfo3.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

// https://stackoverflow.com/questions/37320870/is-there-a-uuid-validator-annotation
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy={})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp="^[a-zA-Z\\(\\)\s]+$", message="Query must be in the latin alphabet")
public @interface ValidSearch {
    String message() default "{invalid.search}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}