package uk.co.onsdigital.discovery.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import uk.co.onsdigital.discovery.dao.parameters.NamedParam;
import uk.co.onsdigital.discovery.exception.UnexpectedErrorException;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.DATABASE_ERROR;
import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.DATASET_ID_MISSING;
import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.STRING_TO_INT_ERROR;
import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.UNEXPECTED_ERROR;

/**
 * {@link DatasetDAO} impl - provides functionality for querying and updating {@link DatasetMetadata}.
 */
@Component
public class DatasetDAOImpl implements DatasetDAO {

    /**
     * Json metadata column name.
     */
    static final String JSON_METADATA_FIELD = "metadata";

    /**
     * Major version column name.
     */
    static final String MAJOR_VERSION_FIELD = "major_version";

    /**
     * Major Label column name.
     */
    static final String MAJOR_LABEL_FIELD = "major_label";

    /**
     * Minor version column name.
     */
    static final String MINOR_VERSION_FIELD = "minor_version";

    /**
     * Revision Notes column name
     */
    static final String REVISION_NOTES_FIELD = "revision_notes";

    /**
     * Revision Reason column name.
     */
    static final String REVISION_REASON_FIELD = "revision_reason";

    /**
     * Dimensional Dataset ID column name.
     */
    static final String DATASET_ID_FIELD = "dimensional_data_set_id";

    static final String DATA_RESOURCE_FIELD = "data_resource";

    /**
     * Query for all DimensionalDataSet IDs.
     */
    static final String DATASET_IDS_QUERY = "SELECT dimensional_data_set_id FROM dimensional_data_set";

    /**
     * Query for a DimensionalDataSet by its ID.
     */
    static final String DATASET_BY_ID_QUERY = "SELECT dimensional_data_set_id, metadata, major_version, major_label," +
            " minor_version, revision_notes, revision_reason, data_resource FROM dimensional_data_set WHERE" +
            " dimensional_data_set_id = :dimensional_data_set_id";

    /**
     * Update statement for persisting new/updating metadata.
     */
    static final String UPDATE_METADATA_QUERY = "UPDATE dimensional_data_set SET metadata = :metadata,  " +
            "major_version = :major_version, major_label = :major_label, minor_version = :minor_version, " +
            "revision_notes = :revision_notes, revision_reason = :revision_reason, data_resource = :data_resource " +
            "WHERE dimensional_data_set_id = :dimensional_data_set_id";

    static final String QUERY_FOR_ALL = "SELECT * FROM dimensional_data_set";

    private static String getStr(ResultSet rs, String key) throws SQLException {
        return isNotEmpty(rs.getString(key)) ? rs.getString(key) : "";
    }

    private static int getInt(ResultSet rs, String key) throws SQLException {
        return rs.getInt(key);
    }

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * {@link RowMapper} implementation for extracting data row data into {@link DatasetMetadata}.
     */
    private RowMapper<DatasetMetadata> metadataRowMapper = (rs, i) ->
            new DatasetMetadata()
                    .setJsonMetadata(getStr(rs, JSON_METADATA_FIELD))
                    .setMajorVersion(getInt(rs, MAJOR_VERSION_FIELD))
                    .setMajorLabel(getStr(rs, MAJOR_LABEL_FIELD))
                    .setMinorVersion(getInt(rs, MINOR_VERSION_FIELD))
                    .setRevisionNotes(getStr(rs, REVISION_NOTES_FIELD))
                    .setRevisionReason(getStr(rs, REVISION_REASON_FIELD))
                    .setDataResource(getStr(rs, DATA_RESOURCE_FIELD))
                    .setDatasetId(getStr(rs, DATASET_ID_FIELD));


    // Function encapsulates the creation of the MapSqlParameterSource. Can be easily be swapped for mock in test.
    private Function<List<NamedParam>, MapSqlParameterSource> createParameterSource = (list) -> {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        for (NamedParam namedParam : list) {
            sqlParameterSource.addValue(namedParam.getKey(), namedParam.getValue());
        }
        return sqlParameterSource;
    };

    @Override
    public List<String> getDatasetIds() throws UnexpectedErrorException {
        try {
            return namedParameterJdbcTemplate.queryForList(DATASET_IDS_QUERY, new MapSqlParameterSource(), String.class);
        } catch (Exception ex) {
            throw new UnexpectedErrorException(DATABASE_ERROR, ex);
        }
    }

    @Override
    public DatasetMetadata getByDatasetId(UUID datasetID) throws UnexpectedErrorException {
        if (datasetID == null) {
            throw new UnexpectedErrorException(DATASET_ID_MISSING);
        }
        try {
            SqlParameterSource sqlParameterSource = createParameterSource.apply(
                    new NamedParam.ListBuilder()
                            .addParam(DATASET_ID_FIELD, datasetID)
                            .toList());
            return namedParameterJdbcTemplate.queryForObject(DATASET_BY_ID_QUERY, sqlParameterSource, metadataRowMapper);
        } catch (DataAccessException ex) {
            throw new UnexpectedErrorException(DATABASE_ERROR, ex);
        }
    }

    @Override
    public List<DatasetMetadata> getAll() throws UnexpectedErrorException {
        List<DatasetMetadata> datasetMetadatas = new ArrayList<>();
        namedParameterJdbcTemplate.query(QUERY_FOR_ALL, new HashMap<>(), (rs) -> {
            datasetMetadatas.add(new DatasetMetadata()
                    .setJsonMetadata(getStr(rs, JSON_METADATA_FIELD))
                    .setMajorVersion(getInt(rs, MAJOR_VERSION_FIELD))
                    .setMajorLabel(getStr(rs, MAJOR_LABEL_FIELD))
                    .setMinorVersion(getInt(rs, MINOR_VERSION_FIELD))
                    .setRevisionNotes(getStr(rs, REVISION_NOTES_FIELD))
                    .setRevisionReason(getStr(rs, REVISION_REASON_FIELD))
                    .setDataResource(getStr(rs, DATA_RESOURCE_FIELD))
                    .setDatasetId(getStr(rs, DATASET_ID_FIELD))
            );
        });
        return datasetMetadatas;
    }

    @Override
    public void createOrUpdate(DatasetMetadata form) throws UnexpectedErrorException {
        try {
            SqlParameterSource sqlParameterSource = createParameterSource.apply(
                    new NamedParam.ListBuilder()
                            .addParam(JSON_METADATA_FIELD, form.getJsonMetadata())
                            .addParam(MAJOR_VERSION_FIELD, form.getMajorVersion())
                            .addParam(MAJOR_LABEL_FIELD, form.getMajorLabel())
                            .addParam(MINOR_VERSION_FIELD, form.getMinorVersion())
                            .addParam(REVISION_NOTES_FIELD, form.getRevisionNotes())
                            .addParam(REVISION_REASON_FIELD, form.getRevisionReason())
                            .addParam(DATA_RESOURCE_FIELD, form.getDataResource())
                            .addParam(DATASET_ID_FIELD, UUID.fromString(form.getDatasetId()))
                            .toList()
            );

            namedParameterJdbcTemplate.update(UPDATE_METADATA_QUERY, sqlParameterSource);
        } catch (Exception ex) {
            UnexpectedErrorException.ErrorCode errorCode = UNEXPECTED_ERROR;

            if (ex instanceof DataAccessException) {
                errorCode = DATABASE_ERROR;
            } else if (ex instanceof NumberFormatException) {
                errorCode = STRING_TO_INT_ERROR;
            }
            throw new UnexpectedErrorException(errorCode, ex);
        }
    }
}


