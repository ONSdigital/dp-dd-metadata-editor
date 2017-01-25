package uk.co.onsdigital.discovery.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

@Component
public class MetadataValidator implements Validator {

    static final String JSON_INVALID_ERR_KEY = "dataset.json.metadata.invalid";
    static final String DATASET_ID_EMPTY_ERR_KEY = "dataset.id.empty";

    @Override
    public boolean supports(Class<?> aClass) {
        return DatasetMetadata.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DatasetMetadata datasetMetadata = (DatasetMetadata) o;

        try {
            new ObjectMapper().readValue(datasetMetadata.getJsonMetadata(), JsonNode.class);
        } catch (Exception e) {
            errors.rejectValue("jsonMetadata", JSON_INVALID_ERR_KEY);
        }

        if (StringUtils.isEmpty(datasetMetadata.getDatasetId())) {
            errors.rejectValue("datasetId", DATASET_ID_EMPTY_ERR_KEY);
        }
    }
}
