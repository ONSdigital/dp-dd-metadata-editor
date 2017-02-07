package uk.co.onsdigital.discovery.exception;

public class DataResourceException extends Exception {

    public DataResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataResourceException(String message) {
        super(message);
    }
}
