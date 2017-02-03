package uk.co.onsdigital.discovery.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.co.onsdigital.discovery.controller.exception.DataResourceValidationException;
import uk.co.onsdigital.discovery.dao.DataResourceDAO;
import uk.co.onsdigital.discovery.model.DataResource;
import uk.co.onsdigital.discovery.validation.ValidationErrors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
public class DataResourceAPI {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DataResourceDAO dataResourceDAO;

    @RequestMapping(value = "/dataResource", method = RequestMethod.POST)
    public ResponseEntity createDataResource(@Valid @RequestBody DataResource dataResource,
                                             BindingResult bindingResult) throws DataResourceValidationException {
        if (bindingResult.hasErrors()) {
            throw new DataResourceValidationException(bindingResult);
        }

        dataResourceDAO.createDataResource(dataResource);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/dataResources", method = RequestMethod.GET)
    public List<DataResource> getDataResources() {
        return dataResourceDAO.getDataResources();
    }


    @ExceptionHandler(value = DataResourceValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrors dataResValidationErr(DataResourceValidationException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ValidationErrors(ex.getBindingResult(), messageSource);
    }
}
