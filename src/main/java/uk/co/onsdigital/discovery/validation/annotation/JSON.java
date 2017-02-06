package uk.co.onsdigital.discovery.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields annotated with this must be valid JSON strings.
 */
@Documented
@Constraint(validatedBy = JSONStringValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSON {

    String message() default "string.json.validation.err";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
