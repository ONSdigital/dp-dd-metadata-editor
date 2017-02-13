package uk.co.onsdigital.discovery.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import uk.co.onsdigital.discovery.exception.UnexpectedErrorException;
import uk.co.onsdigital.discovery.model.CreatedResponse;
import uk.co.onsdigital.discovery.model.DataResource;

import java.io.IOException;
import java.util.Locale;

import static java.text.MessageFormat.format;
import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.JSON_PARSE_ERROR;

public abstract class AbstractBaseAPI {

    public static final String LOCATION_LINK = "<a href=\"{0}\">{1}</a>";
    public static final String LOCATION_HEADER_KEY = "location";
    public static final String CHANGES_SUCCESS_MSG = "changed.success.message";

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResponseEntity<CreatedResponse> createSuccessResponse(String identifier, String messageKey) {
        String locationURL = getLocationURL(identifier);
        String url = format(LOCATION_LINK, locationURL, identifier);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(LOCATION_HEADER_KEY, locationURL);

        String successMSG = messageSource.getMessage(messageKey, new Object[]{url}, Locale.ENGLISH);
        return new ResponseEntity<>(new CreatedResponse(successMSG),
                httpHeaders, HttpStatus.CREATED);
    }

    protected String minifyJSONString(String jsonStr) throws UnexpectedErrorException {
        if (StringUtils.isEmpty(jsonStr)) return jsonStr;
        try {
            return objectMapper.readValue(jsonStr.trim(), JsonNode.class).toString();
        } catch (IOException e) {
            throw new UnexpectedErrorException(JSON_PARSE_ERROR, e);
        }
    }

    protected abstract String getLocationURL(String identifier);

}

