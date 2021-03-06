package uk.co.onsdigital.discovery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.onsdigital.discovery.dao.DataResourceDAO;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.exception.BadRequestException;
import uk.co.onsdigital.discovery.exception.UnexpectedErrorException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * The Metadata editor MVC controller. Provides functionality for getting datasetIDS, getting metadata by datasetID
 * and creating / updating metadata.
 */
@Controller
public class MetadataController {

    static final String MODEL_KEY = "datasetMetadata";
    static final String DATASETS_LIST_KEY = "dataSetList";
    static final String DATA_RESOURCE_LIST = "dataResources";
    static final String UPDATE_DS_VIEW = "updateDatasetMetadata";
    static final String SELECT_DS_VIEW = "selectDataset";

    @Autowired
    private DatasetDAO datasetDAO;

    @Autowired
    private DataResourceDAO dataResourceDAO;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/metadata/{datasetId}")
    public String getExistingMetadata(@PathVariable String datasetId, Model model, HttpServletResponse response)
            throws UnexpectedErrorException, BadRequestException {

        try {
            UUID datasetUUID = UUID.fromString(datasetId);
            model.addAttribute(MODEL_KEY, datasetDAO.getByDatasetId(datasetUUID));
            model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
            model.addAttribute(DATA_RESOURCE_LIST, getDataResources());
            response.setStatus(HttpStatus.OK.value());
            return UPDATE_DS_VIEW;
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("DatasetId invalid UUID", ex);
        }
    }

    @GetMapping(value = "/metadata")
    public String getAllDatasets(Model model) throws UnexpectedErrorException {
        model.addAttribute("datasets", datasetDAO.getAll());


        return SELECT_DS_VIEW;
    }

    private List<String> getDataResources() throws UnexpectedErrorException {
        return dataResourceDAO.getAll()
                .stream()
                .map(dataResource -> dataResource.getDataResourceID())
                .collect(toList());
    }
}
