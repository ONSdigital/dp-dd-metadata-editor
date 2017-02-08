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
import uk.co.onsdigital.discovery.exception.DataResourceException;
import uk.co.onsdigital.discovery.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.model.DatasetMetadata;
import uk.co.onsdigital.discovery.validation.MetadataValidator;

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

    static final String EDITOR_VIEW = "editor";
    static final String MODEL_KEY = "datasetMetadata";
    static final String DATASETS_LIST_KEY = "dataSetList";
    static final String DATA_RESOURCE_LIST = "dataResources";
    static final String UUID_PARAM_NAME = "uuid";
    static final String UPDATE_SUCCESSFUL_FLAG = "updateSuccessful";

    @Autowired
    private MetadataValidator validator;

    @Autowired
    private DatasetDAO datasetDAO;

    @Autowired
    private DataResourceDAO dataResourceDAO;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Return the Editor form.
     */
    @GetMapping("/metadata")
    public String getMetadataForm(Model model, HttpServletResponse response)
            throws MetadataEditorException, DataResourceException {
        model.addAttribute(MODEL_KEY, new DatasetMetadata());
        model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
        model.addAttribute(DATA_RESOURCE_LIST, getDataResources());
        response.setStatus(HttpStatus.OK.value());
        return "createDatasetMetadata";
    }


    @GetMapping("/metadata/{datasetId}")
    public String getExistingMetadata(@PathVariable String datasetId, Model model, HttpServletResponse response)
            throws MetadataEditorException, DataResourceException {

        model.addAttribute(MODEL_KEY, datasetDAO.getMetadataByDatasetId(UUID.fromString(datasetId)));
        model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
        model.addAttribute(DATA_RESOURCE_LIST, getDataResources());
        response.setStatus(HttpStatus.OK.value());
        return "updateDatasetMetadata";
    }

    private List<String> getDataResources() throws DataResourceException {
        return dataResourceDAO.getAll()
                .stream()
                .map(dataResource -> dataResource.getDataResourceID())
                .collect(toList());
    }
}
