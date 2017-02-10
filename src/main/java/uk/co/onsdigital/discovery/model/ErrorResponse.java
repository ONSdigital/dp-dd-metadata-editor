package uk.co.onsdigital.discovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

/**
 * Response object returned by {@link uk.co.onsdigital.discovery.api.MetadataAPI} for any failure scenario.
 */
public class ErrorResponse {

    private static final String ERROR_FMT = "{0}: {1}";

    private int httpStatus;
    private String message;

    public ErrorResponse(Exception ex, HttpStatus status) {
        this.message = MessageFormat.format(ERROR_FMT, ex.getClass().getSimpleName(), ex.getMessage());
        this.httpStatus = status.value();
    }

    public ErrorResponse() {
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public ErrorResponse setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ErrorResponse that = (ErrorResponse) o;

        return new EqualsBuilder()
                .append(getHttpStatus(), that.getHttpStatus())
                .append(getMessage(), that.getMessage())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getHttpStatus())
                .append(getMessage())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("httpStatus", httpStatus)
                .append("message", message)
                .toString();
    }
}
