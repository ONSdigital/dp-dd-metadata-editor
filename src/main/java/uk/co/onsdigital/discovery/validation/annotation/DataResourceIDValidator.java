package uk.co.onsdigital.discovery.validation.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;
import uk.co.onsdigital.discovery.dao.parameters.NamedParam;
import uk.co.onsdigital.discovery.dao.parameters.NamedParameterFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DataResourceIDValidator implements ConstraintValidator<DataResourceID, String> {

    static final String COUNT = "SELECT count(*) FROM data_resource d WHERE d.data_resource = :dataResource";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterFactory namedParameterFactory;

    @Override
    public void initialize(DataResourceID dataResourceID) {
    }

    @Override
    public boolean isValid(String dataResource, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        if (StringUtils.isEmpty(dataResource)) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("dataset.data.resource.empty")
                    .addConstraintViolation();
            return false;
        }

        NamedParam.ListBuilder builder = new NamedParam.ListBuilder().addParam("dataResource", dataResource);
        Integer result = jdbcTemplate.queryForObject(COUNT, namedParameterFactory.create(builder), Integer.class);

        if (result == 0) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("dataset.data.resource.does.not.exist")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
