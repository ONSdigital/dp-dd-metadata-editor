package uk.co.onsdigital.discovery.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.co.onsdigital.discovery.exception.BadRequestException;
import uk.co.onsdigital.discovery.exception.ValidationException;
import uk.co.onsdigital.discovery.exception.UnexpectedErrorException;
import uk.co.onsdigital.discovery.model.ErrorResponse;
import uk.co.onsdigital.discovery.model.ValidationErrorsResponse;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Error handler for REST API.
 */
@RestControllerAdvice
public class APIErrorHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Handle {@link ValidationException}'s
     */
    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorsResponse dataResValidationErr(ValidationException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ValidationErrorsResponse(ex.getBindingResult(), messageSource);
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleDataResourceError(BadRequestException ex, HttpServletResponse response) {
        response.setStatus(BAD_REQUEST.value());
        return new ErrorResponse(ex, BAD_REQUEST);
    }

    @ExceptionHandler(value = UnexpectedErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerMetadataError(UnexpectedErrorException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
