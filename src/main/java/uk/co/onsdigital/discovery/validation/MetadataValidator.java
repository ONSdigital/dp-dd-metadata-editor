package uk.co.onsdigital.discovery.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class MetadataValidator implements Validator {

    static final String JSON_INVALID_ERR_KEY = "dataset.json.metadata.invalid";

    static final String DATASET_ID_EMPTY_ERR_KEY = "dataset.id.empty";
    static final String DATASET_ID_INVALID = "dataset.id.invalid";

    static final String MAJOR_VERSION_NOT_NUMBER = "dataset.major.version.number.format.ex";
    static final String MAJOR_VERSION_EMPTY = "dataset.major.version.empty";

    static final String MINOR_VERSION_NOT_NUMBER = "dataset.minor.version.number.format.ex";

    static final String JSON_METADATA_FIELD_NAME = "jsonMetadata";
    static final String DATASET_ID_FIELD_NAME = "datasetId";
    static final String MAJOR_VERSION_FIELD_NAME = "majorVersion";
    static final String MINOR_VERSION_FIELD_NAME = "minorVersion";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(Class<?> aClass) {
        return DatasetMetadata.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DatasetMetadata metadata = (DatasetMetadata) o;

        validateDatasetId(errors, metadata);
        validMetadataJSON(errors, metadata);
        validateMajorVersion(errors, metadata);
        validateMinorVersion(errors, metadata);
    }

    private void validateDatasetId(Errors errors, DatasetMetadata metadata) {
        if (isEmpty(metadata.getDatasetId())) {
            errors.rejectValue(DATASET_ID_FIELD_NAME, DATASET_ID_EMPTY_ERR_KEY);
        } else {
            try {
                UUID.fromString(metadata.getDatasetId());
            } catch (IllegalArgumentException e) {
                errors.rejectValue(DATASET_ID_FIELD_NAME, DATASET_ID_INVALID);
            }
        }
    }

    private void validMetadataJSON(Errors errors, DatasetMetadata metadata) {
        if (isNotEmpty(metadata.getJsonMetadata())) {
            try {
                objectMapper.readValue(metadata.getJsonMetadata(), JsonNode.class);
            } catch (Exception e) {
                errors.rejectValue(JSON_METADATA_FIELD_NAME, JSON_INVALID_ERR_KEY);
            }
        }
    }

    private void validateMajorVersion(Errors errors, DatasetMetadata metadata) {
        if (isEmpty(metadata.getMajorVersion())) {
            errors.rejectValue(MAJOR_VERSION_FIELD_NAME, MAJOR_VERSION_EMPTY);
        }

        if (isNotEmpty(metadata.getMajorVersion())) {
            try {
                Integer.parseInt(metadata.getMajorVersion());
            } catch (NumberFormatException e) {
                errors.rejectValue(MAJOR_VERSION_FIELD_NAME, MAJOR_VERSION_NOT_NUMBER);
            }
        }
    }

    private void validateMinorVersion(Errors errors, DatasetMetadata metadata) {
        if (isNotEmpty(metadata.getMinorVersion())) {
            try {
                Integer.parseInt(metadata.getMinorVersion());
            } catch (NumberFormatException e) {
                errors.rejectValue(MINOR_VERSION_FIELD_NAME, MINOR_VERSION_NOT_NUMBER);
            }
        }
    }
}
