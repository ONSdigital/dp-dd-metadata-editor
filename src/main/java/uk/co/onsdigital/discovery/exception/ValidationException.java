package uk.co.onsdigital.discovery.exception;

import org.springframework.validation.BindingResult;

/**
 * Exception impl for {@link uk.co.onsdigital.discovery.model.DataResource} validation errors.
 */
public class ValidationException extends Exception {

    private BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult) {
        super("Validation error");
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
