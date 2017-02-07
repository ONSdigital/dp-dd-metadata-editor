package uk.co.onsdigital.discovery.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.text.MessageFormat.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * REST API endpoint for Creating, updating and viewing {@link DataResource}'s.
 */
@RestController
public class DataResourceAPI {

    static final String DATA_RESOURCE_LOCATION = "/dataResource/{0}";
    static final String LOCATION_LINK = "<a href=\"{0}\">{1}</a>";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DataResourceDAO dataResourceDAO;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Create a new {@link DataResource}
     */
    @RequestMapping(value = "/dataResource", method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreatedResponse> createDataResource(@Valid @RequestBody DataResource dataResource,
                                                              BindingResult bindingResult) throws DataResourceException {
        if (bindingResult.hasErrors()) {
            throw new DataResourceValidationException(bindingResult);
        }

        dataResourceDAO.create(minifyJSON(dataResource));
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
        dataResourceDAO.update(minifyJSON(dataResource));
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
        String location = format(DATA_RESOURCE_LOCATION, dataResourceID);
        httpHeaders.add("location", format(DATA_RESOURCE_LOCATION, dataResourceID));
        String successMSG = messageSource.getMessage("data.resource.success",
                new Object[]{format(LOCATION_LINK, location, dataResourceID)}, Locale.ENGLISH);
        return new ResponseEntity<>(new CreatedResponse(successMSG),
                httpHeaders, HttpStatus.CREATED);
    }

    private DataResource minifyJSON(DataResource dataResource) throws DataResourceException {
        if (StringUtils.isEmpty(dataResource.getMetadata())) return dataResource;
        try {
            JsonNode jNode = objectMapper.readValue(dataResource.getMetadata().trim(), JsonNode.class);
            return dataResource.setMetadata(jNode.toString());
        } catch (IOException e) {
            throw new DataResourceException("Failed to minify Data resource json", e);
        }
    }

}
