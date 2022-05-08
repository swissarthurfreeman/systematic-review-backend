package ch.unige.pinfo3.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target( {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UUIDOrNullValidator.class)
public @interface UUIDOrNull {
    String message() default "{invalid.uuid}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
