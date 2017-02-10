package uk.co.onsdigital.discovery.dao;

import uk.co.onsdigital.discovery.exception.UnexpectedErrorException;
import uk.co.onsdigital.discovery.model.DataResource;

import java.util.List;

public interface DataResourceDAO {

    void create(DataResource dataResource) throws UnexpectedErrorException;

    void update(DataResource dataResource) throws UnexpectedErrorException;

    DataResource getByID(String dataResourceID) throws UnexpectedErrorException;

    List<DataResource> getAll() throws UnexpectedErrorException;
}
