package uk.co.onsdigital.discovery.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.co.onsdigital.discovery.exception.DataResourceException;
import uk.co.onsdigital.discovery.exception.ValidataionException;
import uk.co.onsdigital.discovery.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.model.ErrorResponse;
import uk.co.onsdigital.discovery.validation.ValidationErrors;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Error handler for REST API.
 */
@RestControllerAdvice
public class APIErrorHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Handle {@link ValidataionException}'s
     */
    @ExceptionHandler(value = ValidataionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrors dataResValidationErr(ValidataionException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ValidationErrors(ex.getBindingResult(), messageSource);
    }

    /**
     * Handle {@link DataResourceException}'s
     */
    @ExceptionHandler(value = {DataResourceException.class, MetadataEditorException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDataResourceError(DataResourceException ex, HttpServletResponse response) {
        response.setStatus(INTERNAL_SERVER_ERROR.value());
        return new ErrorResponse(ex, INTERNAL_SERVER_ERROR);
    }
}
