package uk.co.onsdigital.discovery.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by dave on 03/02/2017.
 */
public class JSONStringValidator implements ConstraintValidator<JSONString, String> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void initialize(JSONString jsonString) {
    }

    @Override
    public boolean isValid(String json, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(json)) return true;

        try {
            objectMapper.readValue(json.getBytes(), JsonNode.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
