package uk.co.onsdigital.discovery.dao;

import uk.co.onsdigital.discovery.exception.DataResourceException;
import uk.co.onsdigital.discovery.model.DataResource;

import java.util.List;

public interface DataResourceDAO {

    void create(DataResource dataResource) throws DataResourceException;

    DataResource getByID(String dataResourceID) throws DataResourceException;

    List<DataResource> getAll() throws DataResourceException;
}
