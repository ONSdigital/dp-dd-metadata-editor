package uk.co.onsdigital.discovery.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.co.onsdigital.discovery.dao.DataResourceDAO;
import uk.co.onsdigital.discovery.exception.DataResourceException;
import uk.co.onsdigital.discovery.exception.DataResourceValidationException;
import uk.co.onsdigital.discovery.model.CreatedResponse;
import uk.co.onsdigital.discovery.model.DataResource;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class DataResourceAPI {

    static final String DATA_RESOURCE_LOCATION = "/dataResource/%s";

    @Autowired
    private DataResourceDAO dataResourceDAO;

    /**
     * Create a new {@link DataResource}
     */
    @RequestMapping(value = "/dataResource", method = RequestMethod.POST)
    public ResponseEntity<CreatedResponse> createDataResource(@Valid @RequestBody DataResource dataResource,
                                                              BindingResult bindingResult)
            throws DataResourceException {
        if (bindingResult.hasErrors()) {
            throw new DataResourceValidationException(bindingResult);
        }

        dataResourceDAO.create(dataResource);
        return response(dataResource.getDataResourceID());
    }

    /**
     * Update an existing {@link DataResource}.
     */
    @RequestMapping(value = "/dataResource/{dataResourceID}", method = RequestMethod.PUT, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreatedResponse> updateDataResource(@PathVariable String dataResourceID,
                                                              @Valid @RequestBody DataResource dataResource,
                                                              BindingResult bindingResult) throws DataResourceException {
        if (bindingResult.hasErrors()) {
            throw new DataResourceValidationException(bindingResult);
        }
        dataResourceDAO.update(dataResource);
        return response(dataResourceID);
    }

    /**
     * Get an existing {@link DataResource} by its ID.
     */
    @RequestMapping(value = "/dataResource/{dataResourceID}", method = RequestMethod.GET, consumes = APPLICATION_JSON_UTF8_VALUE)
    public DataResource getDataResourceByID(@PathVariable String dataResourceID) throws DataResourceException {
        return dataResourceDAO.getByID(dataResourceID);
    }

    /**
     * Get all existing {@link DataResource}'s
     */
    @RequestMapping(value = "/dataResources", method = RequestMethod.GET)
    public List<DataResource> getDataResources() throws DataResourceException {
        return dataResourceDAO.getAll();
    }

    private ResponseEntity<CreatedResponse> response(String dataResourceID) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("location", format(DATA_RESOURCE_LOCATION, dataResourceID));
        return new ResponseEntity<>(new CreatedResponse("Created"), httpHeaders, HttpStatus.CREATED);
    }

}
