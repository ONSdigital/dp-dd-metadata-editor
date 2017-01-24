package uk.co.onsdigital.discovery.dao;

import uk.co.onsdigital.discovery.model.MetadataForm;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by dave on 24/01/2017.
 */
public interface DatasetDAO {

    List<String> getDatasetIds();

    void createOrUpdateMetadata(MetadataForm form) throws SQLException;
}
