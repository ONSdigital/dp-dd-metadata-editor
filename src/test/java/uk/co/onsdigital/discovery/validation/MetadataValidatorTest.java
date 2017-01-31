package uk.co.onsdigital.discovery.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.DATASET_ID_EMPTY_ERR_KEY;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.DATASET_ID_FIELD_NAME;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.DATASET_ID_INVALID;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.JSON_INVALID_ERR_KEY;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.JSON_METADATA_FIELD_NAME;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.MAJOR_VERSION_EMPTY;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.MAJOR_VERSION_FIELD_NAME;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.MAJOR_VERSION_NOT_NUMBER;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.MINOR_VERSION_FIELD_NAME;
import static uk.co.onsdigital.discovery.validation.MetadataValidator.MINOR_VERSION_NOT_NUMBER;

/**
 * Test verifies the {@link MetadataValidator} behaves correctly for the different validation cases.
 */
public class MetadataValidatorTest {

    @Mock
    private BindingResult mockBindingResult;

    private MetadataValidator validator;
    private DatasetMetadata datasetMetadata;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        validator = new MetadataValidator();
    }

    @Test
    public void shouldRejectIfDatasetIDIsEmpty() {
        datasetMetadata = new DatasetMetadata();

        validator.validate(datasetMetadata, mockBindingResult);

        verify(mockBindingResult, times(1)).rejectValue(DATASET_ID_FIELD_NAME, DATASET_ID_EMPTY_ERR_KEY);
    }

    @Test
    public void shouldRejectIfDatasetIDIsNotValidUUID() {
        datasetMetadata = new DatasetMetadata().setDatasetId("12345");

        validator.validate(datasetMetadata, mockBindingResult);

        verify(mockBindingResult, times(1)).rejectValue(DATASET_ID_FIELD_NAME, DATASET_ID_INVALID);
    }

    @Test
    public void shouldRejectIfJSONMetadataIsInvalidJSON() {
        datasetMetadata = new DatasetMetadata()
                .setDatasetId(UUID.randomUUID().toString())
                .setJsonMetadata("THIS IS NOT JSON");

        validator.validate(datasetMetadata, mockBindingResult);

        verify(mockBindingResult, times(1)).rejectValue(JSON_METADATA_FIELD_NAME, JSON_INVALID_ERR_KEY);
    }

    @Test
    public void shouldRejectIfMajorVersionNotNumber() {
        datasetMetadata = new DatasetMetadata()
                .setDatasetId(UUID.randomUUID().toString())
                .setMajorVersion("THIS IS NOT A NUMBER");

        validator.validate(datasetMetadata, mockBindingResult);

        verify(mockBindingResult, times(1)).rejectValue(MAJOR_VERSION_FIELD_NAME, MAJOR_VERSION_NOT_NUMBER);
    }

    @Test
    public void shouldRejectAndNameEveryInvalidField() {
        datasetMetadata = new DatasetMetadata()
                .setJsonMetadata("THIS IS NOT JSON")
                .setMinorVersion("THIS IS NOT A NUMBER");
        validator.validate(datasetMetadata, mockBindingResult);

        verify(mockBindingResult, times(1)).rejectValue(DATASET_ID_FIELD_NAME, DATASET_ID_EMPTY_ERR_KEY);
        verify(mockBindingResult, times(1)).rejectValue(JSON_METADATA_FIELD_NAME, JSON_INVALID_ERR_KEY);
        verify(mockBindingResult, times(1)).rejectValue(MAJOR_VERSION_FIELD_NAME, MAJOR_VERSION_EMPTY);
        verify(mockBindingResult, times(1)).rejectValue(MINOR_VERSION_FIELD_NAME, MINOR_VERSION_NOT_NUMBER);
        verifyNoMoreInteractions(mockBindingResult);
    }
}
