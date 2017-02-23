package uk.co.onsdigital.discovery.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
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
 * RowMapper impl for {@link DatasetMetadata}
 */
@Component
public class DatasetMetadataRowMapper implements RowMapper<DatasetMetadata> {

    @Override
    public DatasetMetadata mapRow(ResultSet rs, int i) throws SQLException {
        return new DatasetMetadata()
                .setJsonMetadata(getStr(rs, JSON_METADATA_FIELD))
                .setMajorVersion(getInt(rs, MAJOR_VERSION_FIELD))
                .setMajorLabel(getStr(rs, MAJOR_LABEL_FIELD))
                .setMinorVersion(getInt(rs, MINOR_VERSION_FIELD))
                .setRevisionNotes(getStr(rs, REVISION_NOTES_FIELD))
                .setRevisionReason(getStr(rs, REVISION_REASON_FIELD))
                .setDataResource(getStr(rs, DATA_RESOURCE_FIELD))
                .setDatasetId(getStr(rs, DATASET_ID_FIELD))
                .setTitle(getStr(rs, TITLE_FIELD));
    }

    private String getStr(ResultSet rs, String key) throws SQLException {
        return isNotEmpty(rs.getString(key)) ? rs.getString(key) : "";
    }

    private int getInt(ResultSet rs, String key) throws SQLException {
        return rs.getInt(key);
    }
}
