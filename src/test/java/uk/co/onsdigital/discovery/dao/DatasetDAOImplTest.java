package uk.co.onsdigital.discovery.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.util.ReflectionTestUtils;
import uk.co.onsdigital.discovery.controller.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static uk.co.onsdigital.discovery.controller.exception.MetadataEditorException.ErrorCode.DATABASE_ERROR;
import static uk.co.onsdigital.discovery.controller.exception.MetadataEditorException.ErrorCode.DATASET_ID_MISSING;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.DATASET_BY_ID_QUERY;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.DATASET_IDS_QUERY;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.DATASET_ID_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.JSON_METADATA_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.MAJOR_VERSION_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.MINOR_VERSION_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.REVISION_NOTES_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.REVISION_REASON_FIELD;
import static uk.co.onsdigital.discovery.dao.DatasetDAOImpl.UPDATE_METADATA_QUERY;

/**
 * Test verifying the behaviour of the {@link DatasetDAOImpl}.
 */
public class DatasetDAOImplTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplateMock;

    @Mock
    private MapSqlParameterSource sqlParameterSourceMock;

    @Mock
    private RowMapper<DatasetMetadata> metadataRowMapperMock;

    private DatasetDAO dao;
    private List<String> datasetIds;
    private int createParameterSourceStubInvocations = 0;
    private List<NamedParam> createParameterSourceStubArgs;
    private Function<List<NamedParam>, MapSqlParameterSource> createParameterSourceStub;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dao = new DatasetDAOImpl();

        this.createParameterSourceStubInvocations = 0;

        this.createParameterSourceStub = (tuples) -> {
            createParameterSourceStubArgs = tuples;
            createParameterSourceStubInvocations++;
            return sqlParameterSourceMock;
        };

        ReflectionTestUtils.setField(dao, "namedParameterJdbcTemplate", this.jdbcTemplateMock);
        ReflectionTestUtils.setField(dao, "createParameterSource", this.createParameterSourceStub);
        ReflectionTestUtils.setField(dao, "metadataRowMapper", this.metadataRowMapperMock);

        this.datasetIds = new ArrayList<>();
        this.datasetIds.add("DS1");
        this.datasetIds.add("DS2");
        this.datasetIds.add("DS3");
    }

    @Test
    public void shouldGetAllDatasetIds() throws Exception {
        given(jdbcTemplateMock.queryForList(eq(DATASET_IDS_QUERY), any(MapSqlParameterSource.class), eq(String.class)))
                .willReturn(datasetIds);

        List<String> ids = dao.getDatasetIds();

        assertThat(ids, equalTo(this.datasetIds));
        verify(jdbcTemplateMock, times(1)).queryForList(eq(DATASET_IDS_QUERY), any(MapSqlParameterSource.class),
                eq(String.class));
    }

    @Test(expected = MetadataEditorException.class)
    public void shouldThrowMetaDataEditorExceptionForAnyError() throws Exception {
        MyDataAccessException exToThrow = new MyDataAccessException("Its broken.");

        given(jdbcTemplateMock.queryForList(any(String.class), any(MapSqlParameterSource.class), any()))
                .willThrow(exToThrow);

        try {
            dao.getDatasetIds();
        } catch (MetadataEditorException ex) {
            assertThat(ex.getErrorCode(), equalTo(DATABASE_ERROR));
            verify(jdbcTemplateMock, times(1))
                    .queryForList(eq(DATASET_IDS_QUERY), any(MapSqlParameterSource.class), eq(String.class));
            throw ex;
        }
    }

    @Test
    public void shouldGetDatasetById() throws Exception {
        UUID datasetId = UUID.randomUUID();
        DatasetMetadata metadata = new DatasetMetadata().setDatasetId(datasetId.toString());

        given(jdbcTemplateMock.queryForObject(eq(DATASET_BY_ID_QUERY), eq(sqlParameterSourceMock), eq(metadataRowMapperMock)))
                .willReturn(metadata);

        DatasetMetadata result = dao.getMetadataByDatasetId(datasetId);

        assertThat(result, equalTo(metadata));
        assertThat(createParameterSourceStubInvocations, equalTo(1));
        verify(jdbcTemplateMock, times(1)).queryForObject(eq(DATASET_BY_ID_QUERY), eq(sqlParameterSourceMock),
                eq(metadataRowMapperMock));
    }

    @Test(expected = MetadataEditorException.class)
    public void getByIDShouldThrowMetadataEditorExceptionForDatasetIDMissing() throws Exception {
        try {
            dao.getMetadataByDatasetId(null);
        } catch (MetadataEditorException ex) {
            assertThat(ex, equalTo(new MetadataEditorException(DATASET_ID_MISSING)));
            assertThat(createParameterSourceStubInvocations, equalTo(0));
            verifyZeroInteractions(jdbcTemplateMock);
            throw ex;
        }
    }

    @Test(expected = MetadataEditorException.class)
    public void getByIDShouldThrowMetadataEditorExceptionForAnyDataAccessException() throws Exception {
        UUID datasetId = UUID.randomUUID();

        given(jdbcTemplateMock.queryForObject(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .willThrow(new MyDataAccessException("ITS BROKEN"));

        List<NamedParam> expectedParams = new NamedParam.ListBuilder()
                .addParam(DATASET_ID_FIELD, datasetId)
                .toList();

        try {
            dao.getMetadataByDatasetId(datasetId);
        } catch (MetadataEditorException e) {
            assertThat(e, equalTo(new MetadataEditorException(DATABASE_ERROR)));
            assertThat(createParameterSourceStubInvocations, equalTo(1));
            assertThat(createParameterSourceStubArgs, equalTo(expectedParams));
            verify(jdbcTemplateMock, times(1))
                    .queryForObject(anyString(), any(SqlParameterSource.class), any(RowMapper.class));
            throw e;
        }
    }

    @Test
    public void shouldCreateOrUpdateDatasetMetadata() throws Exception {
        UUID datasetId = UUID.randomUUID();
        DatasetMetadata metadata = new DatasetMetadata()
                .setDatasetId(datasetId.toString())
                .setMajorVersion("1")
                .setMinorVersion("0")
                .setRevisionNotes("")
                .setRevisionReason("")
                .setJsonMetadata("");

        List<NamedParam> expectedParams = new NamedParam.ListBuilder()
                .addParam(JSON_METADATA_FIELD, "")
                .addParam(MAJOR_VERSION_FIELD, 1)
                .addParam(MINOR_VERSION_FIELD, 0)
                .addParam(REVISION_NOTES_FIELD, "")
                .addParam(REVISION_REASON_FIELD, "")
                .addParam(DATASET_ID_FIELD, datasetId)
                .toList();

        dao.createOrUpdateMetadata(metadata);

        assertThat(createParameterSourceStubInvocations, equalTo(1));
        assertThat(createParameterSourceStubArgs, equalTo(expectedParams));

        verify(jdbcTemplateMock, times(1))
                .update(eq(UPDATE_METADATA_QUERY), eq(sqlParameterSourceMock));
    }

    @Test(expected = MetadataEditorException.class)
    public void createOrUpdateDatasetMetadataShouldThrowMetadataEditorExceptionForDataAccessException()
            throws Exception {

        UUID datasetId = UUID.randomUUID();
        DatasetMetadata metadata = new DatasetMetadata()
                .setDatasetId(datasetId.toString())
                .setMajorVersion("1")
                .setMinorVersion("0")
                .setRevisionNotes("")
                .setRevisionReason("")
                .setJsonMetadata("");

        List<NamedParam> expectedParams = new NamedParam.ListBuilder()
                .addParam(JSON_METADATA_FIELD, "")
                .addParam(MAJOR_VERSION_FIELD, 1)
                .addParam(MINOR_VERSION_FIELD, 0)
                .addParam(REVISION_NOTES_FIELD, "")
                .addParam(REVISION_REASON_FIELD, "")
                .addParam(DATASET_ID_FIELD, datasetId)
                .toList();


        doThrow(new MyDataAccessException("Twang"))
                .when(jdbcTemplateMock).update(UPDATE_METADATA_QUERY, sqlParameterSourceMock);

        try {
            dao.createOrUpdateMetadata(metadata);
        } catch (MetadataEditorException e) {
            assertThat(createParameterSourceStubInvocations, equalTo(1));
            assertThat(createParameterSourceStubArgs, equalTo(expectedParams));
            verify(jdbcTemplateMock, times(1))
                    .update(eq(UPDATE_METADATA_QUERY), eq(sqlParameterSourceMock));
            assertThat(e.getErrorCode(), equalTo(DATABASE_ERROR));
            throw e;
        }
    }

    // Implementation of Abstract exception for tests.
    static class MyDataAccessException extends DataAccessException {
        public MyDataAccessException(String msg) {
            super(msg);
        }
    }
}
