package uk.co.onsdigital.discovery.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.co.onsdigital.discovery.controller.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.model.DatasetMetadata;
import uk.co.onsdigital.discovery.model.ErrorResponse;

import java.util.UUID;

import static uk.co.onsdigital.discovery.controller.exception.MetadataEditorException.ErrorCode.DATASET_ID_MISSING;

@RestController
public class MetadataAPI {

    @Autowired
    private DatasetDAO dao;

    @GetMapping("/metadata/{datasetID}")
    @ResponseStatus(HttpStatus.OK)
    public DatasetMetadata getMetaData(@PathVariable String datasetID) throws MetadataEditorException {
        if (StringUtils.isEmpty(datasetID)) {
            throw new MetadataEditorException(DATASET_ID_MISSING);
        }
        return dao.getMetadataByDatasetId(UUID.fromString(datasetID));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse errorHandler(Exception ex) {
        return new ErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }
}