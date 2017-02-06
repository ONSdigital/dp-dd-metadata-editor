package uk.co.onsdigital.discovery.validation;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ValidationErrors {

    private List<Error> errors;

    public ValidationErrors(BindingResult bindingResult, MessageSource messageSource) {
        errors = new ArrayList<>();
        bindingResult.getAllErrors()
                .stream()
                .forEach(objectErr -> {
                    String message = messageSource.getMessage(objectErr.getDefaultMessage(), null, Locale.ENGLISH);
                    errors.add(new Error(objectErr.getDefaultMessage(), message));
                });
    }

    public List<Error> getErrors() {
        return errors;
    }

    public static class Error {

        private String code;
        private String message;

        public Error(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
