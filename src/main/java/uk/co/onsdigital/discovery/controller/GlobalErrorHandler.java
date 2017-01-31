package uk.co.onsdigital.discovery.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;

/**
 * Error handler handling any errors throw by the MVC application. Returns a customised error page with details of the
 * error.
 */
@ControllerAdvice
@Component
public class GlobalErrorHandler implements ErrorViewResolver {

    static final String ERROR_VIEW = "error";
    static final String ERROR_DETAILS_PARAM_NAME = "errorDetails";
    static final String ERROR_STACKTRACE_PARAM_NAME = "errorStackTrace";
    static final String EASTER_EGG_PARAM_NAME = "easterEgg";
    static final String[] IMAGES = {"/images/fireball.gif", "/images/try_not_to_cry.gif", "/images/bug.gif"};

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String globalErrorHandler(Exception ex, Model model) {
        model.addAttribute(ERROR_DETAILS_PARAM_NAME, ex.getMessage());
        model.addAttribute(ERROR_STACKTRACE_PARAM_NAME, ExceptionUtils.getStackTrace(ex));
        model.addAttribute(EASTER_EGG_PARAM_NAME, getImage());
        return ERROR_VIEW;
    }

    /**
     * Custom Error View resolver - returns a customised 404 Page not found.
     */
    @Override
    public ModelAndView resolveErrorView(HttpServletRequest httpServletRequest, HttpStatus httpStatus, Map<String, Object> map) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.getModel().put(ERROR_DETAILS_PARAM_NAME, "404 Page not found.");
        modelAndView.getModel().put(EASTER_EGG_PARAM_NAME, getImage());
        return modelAndView;
    }

    private String getImage() {
        Random random = new Random();
        int index = random.nextInt(IMAGES.length);
        return IMAGES[index];
    }
}