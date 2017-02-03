package uk.co.onsdigital.discovery.dao;

import uk.co.onsdigital.discovery.model.DataResource;

import java.util.List;

public interface DataResourceDAO {

    void createDataResource(DataResource dataResource);

    List<DataResource> getDataResources();
}
