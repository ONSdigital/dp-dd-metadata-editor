package uk.co.onsdigital.discovery.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.exception.BadRequestException;
import uk.co.onsdigital.discovery.exception.UnexpectedErrorException;
import uk.co.onsdigital.discovery.exception.ValidationException;
import uk.co.onsdigital.discovery.model.CreatedResponse;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import static java.text.MessageFormat.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.DATASET_ID_MISSING;
import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.INVALID_DATASET_UUID;
import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.UNEXPECTED_ERROR;

/**
 * REST endpoint for obtaining {@link DatasetMetadata} by datasetID.
 */
@RestController
public class MetadataAPI extends AbstractBaseAPI {

    private static final String LOCATION_URL = "/metadata/{0}";

    @Autowired
    private DatasetDAO datasetDAO;

    /**
     * Returns a {@link List} of all existing {@link DatasetMetadata}'s.
     */
    @GetMapping(value = "/metadatas", produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<DatasetMetadata> getAll() throws UnexpectedErrorException {
        return datasetDAO.getAll();
    }

    /**
     * Get by {@link DatasetMetadata#datasetId}
     */
    @GetMapping(value = "/metadata/{datasetID}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DatasetMetadata getDatasetMetadata(@PathVariable String datasetID) throws UnexpectedErrorException, BadRequestException {
        if (StringUtils.isEmpty(datasetID)) {
            throw new UnexpectedErrorException(DATASET_ID_MISSING);
        }
        try {
            UUID datasetIDUUID = UUID.fromString(datasetID);
            return datasetDAO.getByDatasetId(datasetIDUUID);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(INVALID_DATASET_UUID.getDetails());
        }
    }

    /**
     * Update an existing {@link DatasetMetadata}
     */
    @PutMapping(value = "/metadata/{datasetId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreatedResponse> updateDatasetMetadata(@PathVariable String datasetId,
                                                                 @Valid @RequestBody DatasetMetadata datasetMetadata,
                                                                 BindingResult bindingResult) throws ValidationException, UnexpectedErrorException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        datasetDAO.createOrUpdate(sanitise(datasetMetadata));
        return createSuccessResponse(datasetId, CHANGES_SUCCESS_MSG);
    }

    @Override
    protected String getLocationURL(String identifier) {
        return format(LOCATION_URL, identifier);
    }

    /**
     * Clean up the form data. Check if the JSON metadata is valid JSON, minify it to remove any unnecessary whitespace.
     */
    private DatasetMetadata sanitise(DatasetMetadata metadata) throws UnexpectedErrorException {
        if (isNotEmpty(metadata.getJsonMetadata())) {
            metadata.setJsonMetadata(minifyJSONString(metadata.getJsonMetadata()));
        }
        if (isNotEmpty(metadata.getMajorLabel())) {
            metadata.setMajorLabel(toCamelCase(metadata.getMajorLabel()));
        }
        if (isNotEmpty(metadata.getRevisionNotes())) {
            metadata.setRevisionNotes(trim(metadata.getRevisionNotes()));
        }
        if (isNotEmpty(metadata.getRevisionReason())) {
            metadata.setRevisionReason(trim(metadata.getRevisionReason()));
        }
        return metadata;
    }

    private String toCamelCase(String input) throws UnexpectedErrorException {
        try {
            if (StringUtils.isEmpty(input)) return input;

            StringBuilder camelCase = new StringBuilder();
            for (String word : input.trim().split(" ")) {

                String[] letters = word.split("");
                for (int i = 0; i < letters.length; i++) {
                    String letter = i == 0 ? letters[i].toUpperCase() : letters[i].toLowerCase();
                    camelCase.append(URLEncoder.encode(letter, "UTF-8"));
                }
            }
            return camelCase.toString();
        } catch (UnsupportedEncodingException e) {
            throw new UnexpectedErrorException(UNEXPECTED_ERROR);
        }
    }
}