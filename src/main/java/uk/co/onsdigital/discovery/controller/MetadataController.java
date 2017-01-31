package uk.co.onsdigital.discovery.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.onsdigital.discovery.controller.exception.MetadataEditorException;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.model.DatasetMetadata;
import uk.co.onsdigital.discovery.validation.MetadataValidator;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.trim;
import static uk.co.onsdigital.discovery.controller.exception.MetadataEditorException.ErrorCode.JSON_PARSE_ERROR;

/**
 * The Metadata editor MVC controller. Provides functionality for getting datasetIDS, getting metadata by datasetID
 * and creating / updating metadata.
 */
@Controller
public class MetadataController {

    static final String EDITOR_VIEW = "editor";
    static final String MODEL_KEY = "datasetMetadata";
    static final String DATASETS_LIST_KEY = "dataSetList";
    static final String UUID_PARAM_NAME = "uuid";
    static final String UPDATE_SUCCESSFUL_FLAG = "updateSuccessful";

    @Autowired
    private MetadataValidator validator;

    @Autowired
    private DatasetDAO datasetDAO;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Return the Editor form.
     */
    @GetMapping("/")
    public String getMetadataForm(Model model, HttpServletResponse response) throws MetadataEditorException {
        model.addAttribute(MODEL_KEY, new DatasetMetadata());
        model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
        response.setStatus(HttpStatus.OK.value());
        return EDITOR_VIEW;
    }

    /**
     * Handle post requests to  update/create dataset metadata. If there are any validation errors then the user is taken
     * back to the editor view and each validation message will be displayed.
     */
    @PostMapping("/")
    public String metadataSubmit(@Valid DatasetMetadata datasetMetadata, Model model, BindingResult bindingResult,
                                 HttpServletResponse response)
            throws MetadataEditorException {
        validator.validate(datasetMetadata, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return EDITOR_VIEW;
        }

        datasetDAO.createOrUpdateMetadata(sanitise(datasetMetadata));
        model.addAttribute(UPDATE_SUCCESSFUL_FLAG, true);
        model.addAttribute(UUID_PARAM_NAME, datasetMetadata.getDatasetId());
        model.addAttribute(MODEL_KEY, new DatasetMetadata());
        model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
        response.setStatus(HttpStatus.CREATED.value());
        return EDITOR_VIEW;
    }

    /**
     * Clean up the form data. Check if the JSON metadata is valid JSON, minify it to remove any unnecessary whitespace.
     */
    private DatasetMetadata sanitise(DatasetMetadata metadata) throws MetadataEditorException {
        if (isNotEmpty(metadata.getJsonMetadata())) {
            try {
                JsonNode jNode = objectMapper.readValue(metadata.getJsonMetadata().trim(), JsonNode.class);
                metadata.setJsonMetadata(jNode.toString());
            } catch (IOException e) {
                throw new MetadataEditorException(JSON_PARSE_ERROR);
            }
        }
        if (isNotEmpty(metadata.getMajorVersion())) {
            metadata.setMajorVersion(trim(metadata.getMajorVersion()));
        }
        if (isNotEmpty(metadata.getMinorVersion())) {
            metadata.setMinorVersion(trim(metadata.getMinorVersion()));
        }
        if (isNotEmpty(metadata.getRevisionNotes())) {
            metadata.setRevisionNotes(trim(metadata.getRevisionNotes()));
        }
        if (isNotEmpty(metadata.getRevisionReason())) {
            metadata.setRevisionReason(trim(metadata.getRevisionReason()));
        }
        return metadata;
    }
}
