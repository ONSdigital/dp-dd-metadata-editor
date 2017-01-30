package uk.co.onsdigital.discovery.dao;

import uk.co.onsdigital.discovery.controller.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.util.List;
import java.util.UUID;

/**
 * Created by dave on 24/01/2017.
 */
public interface DatasetDAO {

    List<String> getDatasetIds() throws MetadataEditorException;

    DatasetMetadata getMetadataByDatasetId(UUID guid) throws MetadataEditorException;

    void createOrUpdateMetadata(DatasetMetadata form) throws MetadataEditorException;
}
