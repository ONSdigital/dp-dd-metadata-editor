package uk.co.onsdigital.discovery.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.co.onsdigital.discovery.model.MetadataForm;

@Component
public class MetadataValidator implements Validator {

    static final String JSON_INVALID_ERR_KEY = "metadata.form.json.invalid";
    static final String DATASET_ID_EMPTY_ERR_KEY = "metadata.form.dataset.id.empty";

    @Override
    public boolean supports(Class<?> aClass) {
        return MetadataForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MetadataForm metadataForm = (MetadataForm) o;

        try {
            new ObjectMapper().readValue(metadataForm.getJson(), JsonNode.class);
        } catch (Exception e) {
            errors.rejectValue("json", JSON_INVALID_ERR_KEY);
        }

        if (StringUtils.isEmpty(metadataForm.getDatasetId())) {
            errors.rejectValue("datasetId", DATASET_ID_EMPTY_ERR_KEY);
        }
    }
}
