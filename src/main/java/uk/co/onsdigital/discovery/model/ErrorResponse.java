package uk.co.onsdigital.discovery.model;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

/**
 * Response object returned by {@link uk.co.onsdigital.discovery.api.MetadataAPI} for any failure scenario.
 */
public class ErrorResponse {

    private static final String ERROR_FMT = "{0}: {1}";

    private int httpStatus;
    private String message;

    public int getHttpStatus() {
        return httpStatus;
    }

    public ErrorResponse(Exception ex, HttpStatus status) {
        this.message = MessageFormat.format(ERROR_FMT, ex.getClass().getSimpleName(), ex.getMessage());
        this.httpStatus = status.value();
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
}