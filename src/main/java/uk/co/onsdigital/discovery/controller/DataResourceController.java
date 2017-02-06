package uk.co.onsdigital.discovery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.co.onsdigital.discovery.model.DataResource;

/**
 * MVC controller to return the Create Data Resource form.
 */
@Controller
public class DataResourceController {

    public static final String CREATE_DATA_RESCOURCE_VIEW = "createDataResource";

    public static final String DATA_RES_MODEL_KEY = "dataResource";

    @GetMapping(value = "/dataResource")
    public String getDataResourceForm(Model model) {
        model.addAttribute(DATA_RES_MODEL_KEY, new DataResource());
        return CREATE_DATA_RESCOURCE_VIEW;
    }
}
