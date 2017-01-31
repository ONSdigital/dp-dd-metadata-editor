package uk.co.onsdigital.discovery.dao;

import uk.co.onsdigital.discovery.controller.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.util.List;
import java.util.UUID;

/**
 * Defines the interface of a DatasetDAO.
 */
public interface DatasetDAO {

    /**
     * @return a list of all DatasetIDs.
     * @throws MetadataEditorException problem getting the DatasetIDs.
     */
    List<String> getDatasetIds() throws MetadataEditorException;

    /**
     * @param datasetID the datasetID to query for.
     * @return Metadata for the specified Dataset by its ID.
     * @throws MetadataEditorException problem getting metadata for the specified ID.
     */
    DatasetMetadata getMetadataByDatasetId(UUID datasetID) throws MetadataEditorException;

    /**
     * Create or update Dataset Metadata.
     *
     * @param form the {@link DatasetMetadata} values to create/update.
     * @throws MetadataEditorException problem updating/create metadata.
     */
    void createOrUpdateMetadata(DatasetMetadata form) throws MetadataEditorException;
}
