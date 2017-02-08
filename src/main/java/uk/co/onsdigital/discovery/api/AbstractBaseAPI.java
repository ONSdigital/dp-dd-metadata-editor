package uk.co.onsdigital.discovery.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import uk.co.onsdigital.discovery.exception.DataResourceException;
import uk.co.onsdigital.discovery.model.CreatedResponse;
import uk.co.onsdigital.discovery.model.DataResource;

import java.io.IOException;
import java.util.Locale;

import static java.text.MessageFormat.format;

public abstract class AbstractBaseAPI {

    protected static final String LOCATION_LINK = "<a href=\"{0}\">{1}</a>";
    protected static final String LOCATION_HEADER_KEY = "location";
    protected static final String CHANGES_SUCCESS_MSG = "changed.success.message";

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResponseEntity<CreatedResponse> response(String identifier, String messageKey) {
        String locationURL = getLocationURL(identifier);
        String url = format(LOCATION_LINK, locationURL, identifier);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(LOCATION_HEADER_KEY, locationURL);

        String successMSG = messageSource.getMessage(messageKey, new Object[]{url}, Locale.ENGLISH);
        return new ResponseEntity<>(new CreatedResponse(successMSG),
                httpHeaders, HttpStatus.CREATED);
    }


    protected DataResource minifyJSON(DataResource dataResource) throws DataResourceException {
        if (StringUtils.isEmpty(dataResource.getMetadata())) return dataResource;
        try {
            JsonNode jNode = objectMapper.readValue(dataResource.getMetadata().trim(), JsonNode.class);
            return dataResource.setMetadata(jNode.toString());
        } catch (IOException e) {
            throw new DataResourceException("Failed to minify Data resource json", e);
        }
    }

    protected abstract String getLocationURL(String identifier);

}

