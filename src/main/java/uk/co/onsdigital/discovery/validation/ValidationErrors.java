package uk.co.onsdigital.discovery.validation;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ValidationErrors {

    private List<String> errors;

    public ValidationErrors(BindingResult bindingResult, MessageSource messageSource) {
        errors = new ArrayList<>();
        bindingResult.getAllErrors()
                .stream()
                .forEach(objectErr -> errors.add(
                        messageSource.getMessage(objectErr.getDefaultMessage(), null, Locale.ENGLISH)
                ));
    }

    public List<String> getErrors() {
        return errors;
    }
}
