package uk.co.onsdigital.discovery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.onsdigital.discovery.dao.DataResourceDAO;
import uk.co.onsdigital.discovery.exception.DataResourceException;
import uk.co.onsdigital.discovery.model.DataResource;

/**
 * MVC controller to return the Create/Update Data Resource form.
 */
@Controller
public class DataResourceController {

    public static final String CREATE_DATA_RESCOURCE_VIEW = "createDataResource";

    public static final String DATA_RES_MODEL_KEY = "dataResource";

    @Autowired
    public DataResourceDAO dataResourceDAO;

    @GetMapping(value = "/dataResource")
    public String getDataResourceForm(Model model) {
        model.addAttribute(DATA_RES_MODEL_KEY, new DataResource());
        return CREATE_DATA_RESCOURCE_VIEW;
    }

    @GetMapping(value = "/dataResource/{dataResourceID}")
    public String getExistingDataResource(@PathVariable String dataResourceID, Model model) throws DataResourceException {
        model.addAttribute(DATA_RES_MODEL_KEY, dataResourceDAO.getByID(dataResourceID));
        return CREATE_DATA_RESCOURCE_VIEW;
    }
}
