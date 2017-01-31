package uk.co.onsdigital.discovery.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import uk.co.onsdigital.discovery.controller.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static uk.co.onsdigital.discovery.controller.exception.MetadataEditorException.ErrorCode.DATABASE_ERROR;
import static uk.co.onsdigital.discovery.controller.exception.MetadataEditorException.ErrorCode.DATASET_ID_MISSING;
import static uk.co.onsdigital.discovery.controller.exception.MetadataEditorException.ErrorCode.STRING_TO_INT_ERROR;
import static uk.co.onsdigital.discovery.controller.exception.MetadataEditorException.ErrorCode.UNEXPECTED_ERROR;

/**
 * {@link DatasetDAO} impl - provides functionality for querying and updating {@link DatasetMetadata}.
 */
@Component
public class DatasetDAOImpl implements DatasetDAO {

    /** Json metadata column name. */
    static final String JSON_METADATA_FIELD = "metadata";

    /** Major version column name. */
    static final String MAJOR_VERSION_FIELD = "major_version";

    /** Minor version column name. */
    static final String MINOR_VERSION_FIELD = "minor_version";

    /** Revision Notes column name */
    static final String REVISION_NOTES_FIELD = "revision_notes";

    /** Revision Reason column name. */
    static final String REVISION_REASON_FIELD = "revision_reason";

    /** Dimensional Dataset ID column name. */
    static final String DATASET_ID_FIELD = "dimensional_data_set_id";

    /**
     * Query for all DimensionalDataSet IDs.
     */
    static final String DATASET_IDS_QUERY = "SELECT dimensional_data_set_id FROM dimensional_data_set";

    /**
     * Query for a DimensionalDataSet by its ID.
     */
    static final String DATASET_BY_ID_QUERY = "SELECT " +
            "dimensional_data_set_id, metadata, major_version, minor_version, revision_notes, revision_reason " +
            "FROM dimensional_data_set WHERE dimensional_data_set_id = :dimensional_data_set_id";

    /**
     * Update statement for persisting new/updating metadata.
     */
    static final String UPDATE_METADATA_QUERY = "UPDATE dimensional_data_set " +
            "SET metadata = :metadata,  major_version = :major_version, minor_version = :minor_version," +
            " revision_notes = :revision_notes, revision_reason = :revision_reason " +
            "WHERE dimensional_data_set_id = :dimensional_data_set_id";

    private static String getStr(ResultSet rs, String key) throws SQLException {
        return isNotEmpty(rs.getString(key)) ? rs.getString(key) : "";
    }

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * {@link RowMapper} implementation for extracting data row data into {@link DatasetMetadata}.
     */
    private RowMapper<DatasetMetadata> metadataRowMapper = (rs, i) ->
            new DatasetMetadata()
                    .setJsonMetadata(getStr(rs, JSON_METADATA_FIELD))
                    .setMajorVersion(getStr(rs, MAJOR_VERSION_FIELD))
                    .setMinorVersion(getStr(rs, MINOR_VERSION_FIELD))
                    .setRevisionNotes(getStr(rs, REVISION_NOTES_FIELD))
                    .setRevisionReason(getStr(rs, REVISION_REASON_FIELD))
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
    public List<String> getDatasetIds() throws MetadataEditorException {
        try {
            return namedParameterJdbcTemplate.queryForList(DATASET_IDS_QUERY, new MapSqlParameterSource(), String.class);
        } catch (Exception ex) {
            throw new MetadataEditorException(DATABASE_ERROR, ex);
        }
    }

    @Override
    public DatasetMetadata getMetadataByDatasetId(UUID datasetID) throws MetadataEditorException {
        if (datasetID == null) {
            throw new MetadataEditorException(DATASET_ID_MISSING);
        }
        try {
            SqlParameterSource sqlParameterSource = createParameterSource.apply(
                    new NamedParam.ListBuilder()
                            .addParam(DATASET_ID_FIELD, datasetID)
                            .toList());
            return namedParameterJdbcTemplate.queryForObject(DATASET_BY_ID_QUERY, sqlParameterSource, metadataRowMapper);
        } catch (DataAccessException ex) {
            throw new MetadataEditorException(DATABASE_ERROR, ex);
        }
    }

    @Override
    public void createOrUpdateMetadata(DatasetMetadata form) throws MetadataEditorException {
        try {
            int minorVersion = isEmpty(form.getMinorVersion()) ? 0 : Integer.parseInt(form.getMinorVersion());

            SqlParameterSource sqlParameterSource = createParameterSource.apply(
                    new NamedParam.ListBuilder()
                            .addParam(JSON_METADATA_FIELD, form.getJsonMetadata())
                            .addParam(MAJOR_VERSION_FIELD, Integer.parseInt(form.getMajorVersion()))
                            .addParam(MINOR_VERSION_FIELD, minorVersion)
                            .addParam(REVISION_NOTES_FIELD, form.getRevisionNotes())
                            .addParam(REVISION_REASON_FIELD, form.getRevisionReason())
                            .addParam(DATASET_ID_FIELD, UUID.fromString(form.getDatasetId()))
                            .toList()
            );

            namedParameterJdbcTemplate.update(UPDATE_METADATA_QUERY, sqlParameterSource);
        } catch (Exception ex) {
            MetadataEditorException.ErrorCode errorCode = UNEXPECTED_ERROR;

            if (ex instanceof DataAccessException) {
                errorCode = DATABASE_ERROR;
            } else if (ex instanceof NumberFormatException) {
                errorCode = STRING_TO_INT_ERROR;
            }
            throw new MetadataEditorException(errorCode, ex);
        }
    }
}


