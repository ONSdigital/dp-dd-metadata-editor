package uk.co.onsdigital.discovery.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields annotated with {@link UUID} must be valid {@link java.util.UUID} strings.
 */
@Documented
@Constraint(validatedBy = UUIDStringValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UUID {

    String message() default "string.uuid.validation.err";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
