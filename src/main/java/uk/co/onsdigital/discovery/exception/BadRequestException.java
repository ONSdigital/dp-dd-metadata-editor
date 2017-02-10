package uk.co.onsdigital.discovery.exception;

/**
 * Created by dave on 10/02/2017.
 */
public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
