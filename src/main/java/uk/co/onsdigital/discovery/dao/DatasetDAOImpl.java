package uk.co.onsdigital.discovery.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 *
 */
@Component
public class DatasetDAOImpl implements DatasetDAO {

    private static final String DATASET_IDS_QUERY = "SELECT dimensional_data_set_id FROM dimensional_data_set";

    private static final String DATASET_BY_ID_QUERY = "SELECT " +
            "dimensional_data_set_id, json_metadata, major_version, minor_version, revision_notes, revision_reason " +
            "FROM dimensional_data_set WHERE dimensional_data_set_id = :dimensional_data_set_id";

    private static final String UPDATE_METADATA_QUERY = "UPDATE dimensional_data_set " +
            "SET json_metadata = :json_metadata,  major_version = :major_version, minor_version = :minor_version," +
            " revision_notes = :revision_notes, revision_reason = :revision_reason " +
            "WHERE dimensional_data_set_id = :dimensional_data_set_id";

    private static final String JSON_METADATA_FIELD = "json_metadata";
    private static final String MAJOR_VERSION_FIELD = "major_version";
    private static final String MINOR_VERSION_FIELD = "minor_version";
    private static final String REVISION_NOTES_FIELD = "revision_notes";
    private static final String REVISION_REASON_FIELD = "revision_reason";
    private static final String DATASET_ID_FIELD = "dimensional_data_set_id";

    private static final RowMapper<DatasetMetadata> metadataRowMapper = (rs, i) ->
            new DatasetMetadata()
                    .setJsonMetadata(getStr(rs, JSON_METADATA_FIELD))
                    .setMajorVersion(getStr(rs, MAJOR_VERSION_FIELD))
                    .setMinorVersion(getStr(rs, MINOR_VERSION_FIELD))
                    .setRevisionNotes(getStr(rs, REVISION_NOTES_FIELD))
                    .setRevisionReason(getStr(rs, REVISION_REASON_FIELD))
                    .setDatasetId(getStr(rs, DATASET_ID_FIELD));

    private static String getStr(ResultSet rs, String key) throws SQLException {
        return isNotEmpty(rs.getString(key)) ? rs.getString(key) : "";
    }

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<String> getDatasetIds() {
        return namedParameterJdbcTemplate.queryForList(DATASET_IDS_QUERY, new MapSqlParameterSource(), String.class);
    }

    @Override
    public DatasetMetadata getMetadataByDatasetId(UUID guid) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue(DATASET_ID_FIELD, guid);
        return namedParameterJdbcTemplate.queryForObject(DATASET_BY_ID_QUERY, sqlParameterSource, metadataRowMapper);
    }

    @Override
    public void createOrUpdateMetadata(DatasetMetadata form) throws SQLException {
        int minorVersion = "".equals(form.getMinorVersion()) ? 0 : Integer.parseInt(form.getMinorVersion());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(JSON_METADATA_FIELD, form.getJsonMetadata())
                .addValue(MAJOR_VERSION_FIELD, Integer.parseInt(form.getMajorVersion()))
                .addValue(MINOR_VERSION_FIELD, minorVersion)
                .addValue(REVISION_NOTES_FIELD, form.getRevisionNotes())
                .addValue(REVISION_REASON_FIELD, form.getRevisionReason())
                .addValue(DATASET_ID_FIELD, UUID.fromString(form.getDatasetId()));

        namedParameterJdbcTemplate.update(UPDATE_METADATA_QUERY, sqlParameterSource);
    }
}


