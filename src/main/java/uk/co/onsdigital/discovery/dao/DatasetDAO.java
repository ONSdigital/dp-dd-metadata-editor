package uk.co.onsdigital.discovery.dao;

import uk.co.onsdigital.discovery.exception.UnexpectedErrorException;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.util.List;
import java.util.UUID;

/**
 * Defines the interface of a DatasetDAO.
 */
public interface DatasetDAO {

    /**
     * @return a list of all DatasetIDs.
     * @throws UnexpectedErrorException problem getting the DatasetIDs.
     */
    List<String> getDatasetIds() throws UnexpectedErrorException;

    /**
     * @param datasetID the datasetID to query for.
     * @return Metadata for the specified Dataset by its ID.
     * @throws UnexpectedErrorException problem getting metadata for the specified ID.
     */
    DatasetMetadata getByDatasetId(UUID datasetID) throws UnexpectedErrorException;


    List<DatasetMetadata> getAll() throws UnexpectedErrorException;

    /**
     * Create or update Dataset Metadata.
     *
     * @param form the {@link DatasetMetadata} values to create/update.
     * @throws UnexpectedErrorException problem updating/create metadata.
     */
    void createOrUpdate(DatasetMetadata form) throws UnexpectedErrorException;
}
