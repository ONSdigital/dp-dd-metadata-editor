package uk.co.onsdigital.discovery.controller.exception;

import org.springframework.validation.BindingResult;

public class DataResourceValidationException extends Exception {

    private BindingResult bindingResult;

    public DataResourceValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
