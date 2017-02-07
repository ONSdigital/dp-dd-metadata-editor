package uk.co.onsdigital.discovery.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

public class UUIDStringValidator implements ConstraintValidator<UUID, String> {

    @Override
    public void initialize(UUID uuid) {
    }

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext constraintValidatorContext) {
        if (isEmpty(uuid)) {
            return false;
        } else {
            try {
                java.util.UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }
}
