package uk.co.onsdigital.discovery.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import uk.co.onsdigital.discovery.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static uk.co.onsdigital.discovery.exception.MetadataEditorException.ErrorCode.DATASET_ID_MISSING;

/**
 * Test verifying the behaviour of {@link MetadataAPI}.
 */
public class MetadataAPITest {

    @Mock
    private DatasetDAO datasetDAOMock;

    private MetadataAPI api;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        api = new MetadataAPI();

        ReflectionTestUtils.setField(api, "datasetDAO", datasetDAOMock);
    }

    @Test
    public void shouldGetDatasetMetadata() throws Exception {
        UUID datasetID = UUID.randomUUID();
        DatasetMetadata metadata = new DatasetMetadata().setDatasetId(datasetID.toString());

        String json = new ObjectMapper().writeValueAsString(metadata);

        given(datasetDAOMock.getMetadataByDatasetId(datasetID))
                .willReturn(metadata);

        DatasetMetadata result = api.getMetaData(datasetID.toString());

        assertThat(result, equalTo(metadata));
        verify(datasetDAOMock, times(1)).getMetadataByDatasetId(datasetID);
    }

    @Test(expected = MetadataEditorException.class)
    public void shouldThrowMetadataEditorExceptionForDatasetIdNull() throws Exception {
        try {
            api.getMetaData(null);
        } catch (MetadataEditorException e) {
            verifyZeroInteractions(datasetDAOMock);
            assertThat(e.getErrorCode(), equalTo(DATASET_ID_MISSING));
            throw e;
        }
    }

}
