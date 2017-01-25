package uk.co.onsdigital.discovery.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;
import uk.co.onsdigital.discovery.model.MetadataForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by dave on 24/01/2017.
 */
@Component
public class DatasetDAOImpl implements DatasetDAO {

    private static final String DATASET_IDS_QUERY = "SELECT dimensional_data_set_id FROM dimensional_data_set";
    private static final String DATASET_BY_ID_QUERY = "SELECT json_metadata FROM dimensional_data_set WHERE dimensional_data_set_id = ?";
    private static final String UPDATE_METADATA_QUERY = "UPDATE dimensional_data_set SET json_metadata = ? WHERE dimensional_data_set_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getDatasetIds() {
        return jdbcTemplate.queryForList(DATASET_IDS_QUERY, String.class);
    }

    @Override
    public String getJsonMetadataByDatasetId(UUID guid) {
        return jdbcTemplate.queryForObject(DATASET_BY_ID_QUERY, new Object[]{guid}, String.class);
    }

    @Override
    public void createOrUpdateMetadata(MetadataForm form) throws SQLException {
        Connection conn = jdbcTemplate.getDataSource().getConnection();
        PreparedStatementCreator psc = (connection) -> {
            PreparedStatement ps = conn.prepareStatement(UPDATE_METADATA_QUERY);
            ps.setString(1, form.getJson());
            ps.setObject(2, UUID.fromString(form.getDatasetId()));
            return ps;
        };
        jdbcTemplate.update(psc);
    }
}
