package uk.co.onsdigital.discovery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.model.MetadataForm;
import uk.co.onsdigital.discovery.validation.MetadataValidator;

import javax.validation.Valid;

/**
 * Created by dave on 23/01/2017.
 */
@Controller
public class Metadata {

    static final String EDITOR_VIEW = "editor";
    static final String CREATE_SUCCESS_VIEW = "createSuccess";
    static final String MODEL_KEY = "metadataForm";
    static final String DATASETS_LIST_KEY = "dataSetList";

    @Autowired
    private MetadataValidator validator;

    @Autowired
    private DatasetDAO datasetDAO;

    @GetMapping("/metadata")
    public String getMetadataForm(Model model) {
        model.addAttribute(MODEL_KEY, new MetadataForm());
        model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
        return EDITOR_VIEW;
    }

    @PostMapping("/metadata")
    public String metadataSubmit(@Valid MetadataForm metadataForm, Model model, BindingResult bindingResult) throws Exception {
        validator.validate(metadataForm, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(DATASETS_LIST_KEY, datasetDAO.getDatasetIds());
            return EDITOR_VIEW;
        }
        datasetDAO.createOrUpdateMetadata(metadataForm);
        return CREATE_SUCCESS_VIEW;
    }
}
