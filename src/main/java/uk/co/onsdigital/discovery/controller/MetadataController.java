package uk.co.onsdigital.discovery.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.model.DatasetMetadata;
import uk.co.onsdigital.discovery.model.ErrorResponse;
import uk.co.onsdigital.discovery.validation.MetadataValidator;

import javax.validation.Valid;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 *
 */
@Controller
public class MetadataController {

    static final String EDITOR_VIEW = "editor";
    static final String MODEL_KEY = "datasetMetadata";
    static final String DATASETS_LIST_KEY = "dataSetList";

    @Autowired
    private MetadataValidator validator;

    @Autowired
    private DatasetDAO datasetDAO;

    @GetMapping("/")
    public String getMetadataForm(Model model) {
        model.addAttribute(MODEL_KEY, new DatasetMetadata());
        model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
        return EDITOR_VIEW;
    }

    @PostMapping("/")
    public String metadataSubmit(@Valid DatasetMetadata datasetMetadata, Model model, BindingResult bindingResult)
            throws Exception {
        validator.validate(datasetMetadata, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
            return EDITOR_VIEW;
        }

        datasetDAO.createOrUpdateMetadata(sanitise(datasetMetadata));
        model.addAttribute("updateSuccessful", true);
        model.addAttribute("uuid", datasetMetadata.getDatasetId());
        model.addAttribute(MODEL_KEY, new DatasetMetadata());
        model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
        return EDITOR_VIEW;
    }

    private DatasetMetadata sanitise(DatasetMetadata metadata) throws IOException {
        if (isNotEmpty(metadata.getJsonMetadata())) {
            ObjectMapper m = new ObjectMapper();
            JsonNode jNode = m.readValue(metadata.getJsonMetadata().trim(), JsonNode.class);
            metadata.setJsonMetadata(jNode.toString());
        }

        if (isNotEmpty(metadata.getMajorVersion())) {
            metadata.setMajorVersion(metadata.getMajorVersion().trim());
        }

        if (isNotEmpty(metadata.getMinorVersion())) {
            metadata.setMinorVersion(metadata.getMinorVersion().trim());
        }
        return metadata;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse globalErrorHandler(Exception ex) {
        return new ErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }
}
