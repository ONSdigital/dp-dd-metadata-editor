package uk.co.onsdigital.discovery.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.model.DatasetMetadata;
import uk.co.onsdigital.discovery.validation.MetadataValidator;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MetadataController.class)
public class MetadataControllerTest {

    @MockBean
    private MetadataValidator mockValidator;

    @MockBean
    private Model mockModel;

    @MockBean
    private BindingResult mockBindingResult;

    @MockBean
    private DatasetDAO mockDatasetDAO;

    @Autowired
    private MockMvc mvc;

    private MetadataController api;
    private List<String> datasetIds;

    @Before
    public void setUp() throws Exception {
        datasetIds = new ArrayList<>();
        datasetIds.add("DS1");
        datasetIds.add("DS2");
        datasetIds.add("DS3");

        api = new MetadataController();
    }

    @Test
    public void shouldGetDatasetIds() throws Exception {
        given(this.mockDatasetDAO.getDatasetIds())
                .willReturn(datasetIds);

        ModelAndView modelAndView = this.mvc.perform(MockMvcRequestBuilders.get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("editor"))
                .andReturn().getModelAndView();

        List<String> datasetIds = convertToStringList(modelAndView.getModel().get(MetadataController.DATASETS_LIST_KEY));
        assertThat(datasetIds, equalTo(this.datasetIds));

        DatasetMetadata metadata = (DatasetMetadata) modelAndView.getModel().get(MetadataController.MODEL_KEY);
        assertThat(metadata, equalTo(new DatasetMetadata()));
    }

    private List<String> convertToStringList(Object obj) {
        return (List<String>) obj;
    }

}
