package uk.co.onsdigital.discovery.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.co.onsdigital.discovery.dao.DatasetDAO;

import java.util.UUID;

/**
 * Created by dave on 24/01/2017.
 */
@RestController
public class MetadataAPI {

    @Autowired
    private DatasetDAO dao;

    @GetMapping("/metadata/{datasetID}")
    public String getMetaData(@PathVariable String datasetID) {
        return dao.getJsonMetadataByDatasetId(UUID.fromString(datasetID));
    }
}
