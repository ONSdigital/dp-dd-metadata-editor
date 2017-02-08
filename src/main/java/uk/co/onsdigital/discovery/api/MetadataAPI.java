package uk.co.onsdigital.discovery.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.exception.ValidataionException;
import uk.co.onsdigital.discovery.model.CreatedResponse;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.text.MessageFormat.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.trim;
import static uk.co.onsdigital.discovery.exception.MetadataEditorException.ErrorCode.DATASET_ID_MISSING;
import static uk.co.onsdigital.discovery.exception.MetadataEditorException.ErrorCode.JSON_PARSE_ERROR;

/**
 * REST endpoint for obtaining {@link DatasetMetadata} by datasetID.
 */
@RestController
public class MetadataAPI extends AbstractBaseAPI {

    private static final String LOCATION_URL = "/metadata/{0}";

    @Autowired
    private DatasetDAO datasetDAO;

    @GetMapping(value = "/metadatas")
    @ResponseStatus(HttpStatus.OK)
    public List<DatasetMetadata> getAll() throws MetadataEditorException {
        return datasetDAO.getAll();
    }

    @GetMapping(value = "/metadata/{datasetID}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DatasetMetadata getMetaData(@PathVariable String datasetID) throws MetadataEditorException {
        if (StringUtils.isEmpty(datasetID)) {
            throw new MetadataEditorException(DATASET_ID_MISSING);
        }
        return datasetDAO.getMetadataByDatasetId(UUID.fromString(datasetID));
    }

    @PostMapping(value = "/metadata", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreatedResponse> createDatasetMetadata(@Valid @RequestBody DatasetMetadata datasetMetadata,
                                                                 BindingResult bindingResult) throws ValidataionException {
        if (bindingResult.hasErrors()) {
            throw new ValidataionException(bindingResult);
        }
        return response(datasetMetadata.getDatasetId(), CHANGES_SUCCESS_MSG);
    }

    @PutMapping(value = "/metadata/{datasetId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreatedResponse> updateDatasetMetadata(@PathVariable String datasetId,
                                                                 @Valid @RequestBody DatasetMetadata datasetMetadata,
                                                                 BindingResult bindingResult) throws ValidataionException {
        if (bindingResult.hasErrors()) {
            throw new ValidataionException(bindingResult);
        }
        return response(datasetMetadata.getDatasetId(), CHANGES_SUCCESS_MSG);
    }

    @Override
    protected String getLocationURL(String identifier) {
        return format(LOCATION_URL, identifier);
    }

    /**
     * Clean up the form data. Check if the JSON metadata is valid JSON, minify it to remove any unnecessary whitespace.
     */
    private DatasetMetadata sanitise(DatasetMetadata metadata) throws MetadataEditorException {
        if (isNotEmpty(metadata.getJsonMetadata())) {
            try {
                JsonNode jNode = objectMapper.readValue(metadata.getJsonMetadata().trim(), JsonNode.class);
                metadata.setJsonMetadata(jNode.toString());
            } catch (IOException e) {
                throw new MetadataEditorException(JSON_PARSE_ERROR);
            }
        }
        if (isNotEmpty(metadata.getMajorVersion())) {
            metadata.setMajorVersion(trim(metadata.getMajorVersion()));
        }
        if (isNotEmpty(metadata.getMinorVersion())) {
            metadata.setMinorVersion(trim(metadata.getMinorVersion()));
        }
        if (isNotEmpty(metadata.getRevisionNotes())) {
            metadata.setRevisionNotes(trim(metadata.getRevisionNotes()));
        }
        if (isNotEmpty(metadata.getRevisionReason())) {
            metadata.setRevisionReason(trim(metadata.getRevisionReason()));
        }
        return metadata;
    }
}