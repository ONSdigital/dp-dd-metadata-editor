package uk.co.onsdigital.discovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ValidationErrorsResponse {

    private List<ValidationError> errors;

    public ValidationErrorsResponse(BindingResult bindingResult, MessageSource messageSource) {
        errors = new ArrayList<>();
        bindingResult.getAllErrors()
                .stream()
                .forEach(objectErr -> {
                    String message = messageSource.getMessage(objectErr.getDefaultMessage(), null, Locale.ENGLISH);
                    errors.add(new ValidationError(objectErr.getDefaultMessage(), message));
                });
    }

    public ValidationErrorsResponse() {
    }

    public ValidationErrorsResponse(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ValidationErrorsResponse that = (ValidationErrorsResponse) o;

        return new EqualsBuilder()
                .append(getErrors(), that.getErrors())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getErrors())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("errors", errors)
                .toString();
    }
}
