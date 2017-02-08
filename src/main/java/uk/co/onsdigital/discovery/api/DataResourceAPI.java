package uk.co.onsdigital.discovery.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.co.onsdigital.discovery.dao.DataResourceDAO;
import uk.co.onsdigital.discovery.exception.DataResourceException;
import uk.co.onsdigital.discovery.exception.ValidataionException;
import uk.co.onsdigital.discovery.model.CreatedResponse;
import uk.co.onsdigital.discovery.model.DataResource;

import javax.validation.Valid;
import java.util.List;

import static java.text.MessageFormat.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * REST API endpoint for Creating, updating and viewing {@link DataResource}'s.
 */
@RestController
public class DataResourceAPI extends AbstractBaseAPI {

    private static final String LOCATION_URL = "/dataResource/{0}";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DataResourceDAO dataResourceDAO;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Create a new {@link DataResource}
     */
    @RequestMapping(value = "/dataResource", method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreatedResponse> createDataResource(@Valid @RequestBody DataResource dataResource,
                                                              BindingResult bindingResult) throws DataResourceException {
        if (bindingResult.hasErrors()) {
            throw new ValidataionException(bindingResult);
        }

        dataResourceDAO.create(minifyJSON(dataResource));
        return response(dataResource.getDataResourceID(), CHANGES_SUCCESS_MSG);
    }

    /**
     * Update an existing {@link DataResource}.
     */
    @RequestMapping(value = "/dataResource/{dataResourceID}", method = RequestMethod.PUT,
            consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreatedResponse> updateDataResource(@PathVariable String dataResourceID,
                                                              @Valid @RequestBody DataResource dataResource,
                                                              BindingResult bindingResult) throws DataResourceException {
        if (bindingResult.hasErrors()) {
            throw new ValidataionException(bindingResult);
        }
        dataResourceDAO.update(minifyJSON(dataResource));
        return response(dataResourceID, CHANGES_SUCCESS_MSG);
    }

    /**
     * Get an existing {@link DataResource} by its ID.
     */
    @RequestMapping(value = "/dataResource/{dataResourceID}", method = RequestMethod.GET,
            consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public DataResource getDataResourceByID(@PathVariable String dataResourceID) throws DataResourceException {
        return dataResourceDAO.getByID(dataResourceID);
    }

    /**
     * Get all existing {@link DataResource}'s
     */
    @RequestMapping(value = "/dataResources", method = RequestMethod.GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public List<DataResource> getDataResources() throws DataResourceException {
        return dataResourceDAO.getAll();
    }

    @Override
    protected String getLocationURL(String identifier) {
        return format(LOCATION_URL, identifier);
    }
}
