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

/**
 *
 */
@Component
public class DatasetDAOImpl implements DatasetDAO {

    static final String JSON_METADATA_FIELD = "metadata";
    static final String MAJOR_VERSION_FIELD = "major_version";
    static final String MINOR_VERSION_FIELD = "minor_version";
    static final String REVISION_NOTES_FIELD = "revision_notes";
    static final String REVISION_REASON_FIELD = "revision_reason";
    static final String DATASET_ID_FIELD = "dimensional_data_set_id";

    /**
     * QUERY - Get all DimensionalDataSet IDs.
     */
    static final String DATASET_IDS_QUERY = "SELECT dimensional_data_set_id FROM dimensional_data_set";

    /**
     * SQL - Get DimensionalDataSet by its ID.
     */
    static final String DATASET_BY_ID_QUERY = "SELECT " +
            "dimensional_data_set_id, metadata, major_version, minor_version, revision_notes, revision_reason " +
            "FROM dimensional_data_set WHERE dimensional_data_set_id = :dimensional_data_set_id";

    static final String UPDATE_METADATA_QUERY = "UPDATE dimensional_data_set " +
            "SET metadata = :metadata,  major_version = :major_version, minor_version = :minor_version," +
            " revision_notes = :revision_notes, revision_reason = :revision_reason " +
            "WHERE dimensional_data_set_id = :dimensional_data_set_id";

    private static String getStr(ResultSet rs, String key) throws SQLException {
        return isNotEmpty(rs.getString(key)) ? rs.getString(key) : "";
    }

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    private RowMapper<DatasetMetadata> metadataRowMapper = (rs, i) ->
            new DatasetMetadata()
                    .setJsonMetadata(getStr(rs, JSON_METADATA_FIELD))
                    .setMajorVersion(getStr(rs, MAJOR_VERSION_FIELD))
                    .setMinorVersion(getStr(rs, MINOR_VERSION_FIELD))
                    .setRevisionNotes(getStr(rs, REVISION_NOTES_FIELD))
                    .setRevisionReason(getStr(rs, REVISION_REASON_FIELD))
                    .setDatasetId(getStr(rs, DATASET_ID_FIELD));


    // Function encapsulates the creation of the MapSqlParameterSource. Can be easily be swapped for mock in test.
    private Function<NamedParam[], MapSqlParameterSource> createParameterSource = (tuples) -> {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        for (NamedParam namedParam : tuples) {
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
    public DatasetMetadata getMetadataByDatasetId(UUID guid) throws MetadataEditorException {
        if (guid == null) {
            throw new MetadataEditorException(DATASET_ID_MISSING);
        }
        try {
            SqlParameterSource sqlParameterSource = createParameterSource.apply(toArray(namedParam(DATASET_ID_FIELD, guid)));
            return namedParameterJdbcTemplate.queryForObject(DATASET_BY_ID_QUERY, sqlParameterSource, metadataRowMapper);
        } catch (DataAccessException ex) {
            throw new MetadataEditorException(DATABASE_ERROR, ex);
        }
    }

    @Override
    public void createOrUpdateMetadata(DatasetMetadata form) throws MetadataEditorException {
        try {
            int minorVersion = isEmpty(form.getMinorVersion()) ? 0 : Integer.parseInt(form.getMinorVersion());

            SqlParameterSource sqlParameterSource = createParameterSource.apply(toArray(
                    namedParam(JSON_METADATA_FIELD, form.getJsonMetadata()),
                    namedParam(MAJOR_VERSION_FIELD, Integer.parseInt(form.getMajorVersion())),
                    namedParam(MINOR_VERSION_FIELD, minorVersion),
                    namedParam(REVISION_NOTES_FIELD, form.getRevisionNotes()),
                    namedParam(REVISION_REASON_FIELD, form.getRevisionReason()),
                    namedParam(DATASET_ID_FIELD, UUID.fromString(form.getDatasetId()))
            ));

            namedParameterJdbcTemplate.update(UPDATE_METADATA_QUERY, sqlParameterSource);
        } catch (DataAccessException ex) {
            throw new MetadataEditorException(DATABASE_ERROR, ex);
        }
    }

    private NamedParam namedParam(String key, Object value) {
        return new NamedParam(key, value);
    }

    private NamedParam[] toArray(NamedParam... p) {
        return p;
    }
}


