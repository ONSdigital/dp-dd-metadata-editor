package uk.co.onsdigital.discovery.dao;

import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by dave on 24/01/2017.
 */
public interface DatasetDAO {

    List<String> getDatasetIds();

    DatasetMetadata getMetadataByDatasetId(UUID guid);

    void createOrUpdateMetadata(DatasetMetadata form) throws SQLException;
}
