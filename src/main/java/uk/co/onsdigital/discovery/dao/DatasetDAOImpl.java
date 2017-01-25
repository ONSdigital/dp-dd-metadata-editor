package uk.co.onsdigital.discovery.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    private static final String DATASET_BY_ID_QUERY = "SELECT dimensional_data_set_id, json_metadata, major_version," +
            " minor_version  FROM dimensional_data_set WHERE dimensional_data_set_id = ?";

    private static final String UPDATE_METADATA_QUERY = "UPDATE dimensional_data_set SET json_metadata = ? " +
            "WHERE dimensional_data_set_id = ?";

    private static final RowMapper<DatasetMetadata> rowMapper = (rs, i) ->
            new DatasetMetadata()
                    .setJsonMetadata(getStr(rs, "json_metadata"))
                    .setDatasetId(getStr(rs, "dimensional_data_set_id"))
                    .setMajorVersion(getStr(rs, "major_version"))
                    .setMinorVersion(getStr(rs, "minor_version"));

    private static String getStr(ResultSet rs, String key) throws SQLException {
        return isNotEmpty(rs.getString(key)) ? rs.getString(key) : "";
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getDatasetIds() {
        return jdbcTemplate.queryForList(DATASET_IDS_QUERY, String.class);
    }

    @Override
    public DatasetMetadata getMetadataByDatasetId(UUID guid) {
        return jdbcTemplate.queryForObject(DATASET_BY_ID_QUERY, rowMapper, guid);
    }

    @Override
    public void createOrUpdateMetadata(DatasetMetadata form) throws SQLException {
        Connection conn = jdbcTemplate.getDataSource().getConnection();
        PreparedStatementCreator psc = (connection) -> {
            PreparedStatement ps = conn.prepareStatement(UPDATE_METADATA_QUERY);
            ps.setString(1, form.getJsonMetadata());
            ps.setObject(2, UUID.fromString(form.getDatasetId()));
            return ps;
        };
        jdbcTemplate.update(psc);
    }
}


