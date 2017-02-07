package uk.co.onsdigital.discovery.exception;

import org.springframework.validation.BindingResult;

/**
 * Exception impl for {@link uk.co.onsdigital.discovery.model.DataResource} validation errors.
 */
public class DataResourceValidationException extends DataResourceException {

    private BindingResult bindingResult;

    public DataResourceValidationException(BindingResult bindingResult) {
        super("DataResource validation error.");
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
