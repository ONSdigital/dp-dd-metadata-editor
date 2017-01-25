package uk.co.onsdigital.discovery.dao;

import uk.co.onsdigital.discovery.model.MetadataForm;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by dave on 24/01/2017.
 */
public interface DatasetDAO {

    List<String> getDatasetIds();

    String getJsonMetadataByDatasetId(UUID guid);

    void createOrUpdateMetadata(MetadataForm form) throws SQLException;
}
