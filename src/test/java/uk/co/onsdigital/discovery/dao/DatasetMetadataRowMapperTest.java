package uk.co.onsdigital.discovery.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.RowMapper;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.sql.ResultSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.DATASET_ID_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.DATA_RESOURCE_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.JSON_METADATA_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.MAJOR_LABEL_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.MAJOR_VERSION_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.MINOR_VERSION_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.REVISION_NOTES_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.REVISION_REASON_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.TITLE_FIELD;

/**
 * Test the {@link DatasetMetadataRowMapper}
 */
public class DatasetMetadataRowMapperTest {

    @Mock
    private ResultSet resultSetMock;

    private RowMapper<DatasetMetadata> rowMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        rowMapper = new DatasetMetadataRowMapper();

        given(resultSetMock.getString(DATASET_ID_FIELD))
                .willReturn(DATASET_ID_FIELD);

        given(resultSetMock.getString(JSON_METADATA_FIELD))
                .willReturn(JSON_METADATA_FIELD);

        given(resultSetMock.getInt(MAJOR_VERSION_FIELD))
                .willReturn(1);

        given(resultSetMock.getString(MAJOR_LABEL_FIELD))
                .willReturn(MAJOR_LABEL_FIELD);

        given(resultSetMock.getInt(MINOR_VERSION_FIELD))
                .willReturn(1);

        given(resultSetMock.getString(REVISION_NOTES_FIELD))
                .willReturn(REVISION_NOTES_FIELD);

        given(resultSetMock.getString(REVISION_REASON_FIELD))
                .willReturn(REVISION_REASON_FIELD);

        given(resultSetMock.getString(DATA_RESOURCE_FIELD))
                .willReturn(DATA_RESOURCE_FIELD);

        given(resultSetMock.getString(TITLE_FIELD))
                .willReturn(TITLE_FIELD);
    }

    @Test
    public void shouldMapDatasetCorrectly() throws Exception {
        DatasetMetadata expected = new DatasetMetadata()
                .setDatasetId(DATASET_ID_FIELD)
                .setJsonMetadata(JSON_METADATA_FIELD)
                .setMinorVersion(1)
                .setMajorLabel(MAJOR_LABEL_FIELD)
                .setMajorVersion(1)
                .setRevisionNotes(REVISION_NOTES_FIELD)
                .setRevisionReason(REVISION_REASON_FIELD)
                .setDataResource(DATA_RESOURCE_FIELD)
                .setTitle(TITLE_FIELD);

        DatasetMetadata actual = rowMapper.mapRow(resultSetMock, 1);
        assertThat(expected, equalTo(actual));
    }
}
