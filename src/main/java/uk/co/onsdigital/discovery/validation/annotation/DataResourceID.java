package uk.co.onsdigital.discovery.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DataResourceIDValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataResourceID {

    String message() default "dataset.data.resource.empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
