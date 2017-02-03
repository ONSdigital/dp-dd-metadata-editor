package uk.co.onsdigital.discovery.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static uk.co.onsdigital.discovery.controller.GlobalErrorHandler.ERROR_DETAILS_PARAM_NAME;
import static uk.co.onsdigital.discovery.controller.GlobalErrorHandler.ERROR_VIEW;
import static uk.co.onsdigital.discovery.controller.MetadataController.EDITOR_VIEW;
import static uk.co.onsdigital.discovery.controller.MetadataController.MODEL_KEY;

@RunWith(SpringRunner.class)
@WebMvcTest(MetadataController.class)
public class MetadataControllerTest {

    @Autowired
    private MetadataValidator validator;

    @MockBean
    private Model mockModel;

    @MockBean
    private BindingResult mockBindingResult;

    @MockBean
    private DatasetDAO mockDatasetDAO;

    @MockBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

        ModelAndView modelAndView = this.mvc.perform(get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(EDITOR_VIEW))
                .andReturn().getModelAndView();

        List<String> datasetIds = convertToStringList(modelAndView.getModel().get(MetadataController.DATASETS_LIST_KEY));
        assertThat(datasetIds, equalTo(this.datasetIds));

        DatasetMetadata metadata = (DatasetMetadata) modelAndView.getModel().get(MODEL_KEY);
        assertThat(metadata, equalTo(new DatasetMetadata()));
    }

    @Test
    public void shouldReturnErrorForDataAccessException() throws Exception {
        String expectedErrorMessage = "EmptyResultDataAccessException: No datasetID's were found";

        given(mockDatasetDAO.getDatasetIds())
                .willThrow(new EmptyResultDataAccessException(expectedErrorMessage, 0));

        MvcResult mvcResult = this.mvc.perform(get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        ModelAndView mv = mvcResult.getModelAndView();
        assertThat(mv.getViewName(), equalTo(ERROR_VIEW));

        String actualErrorMessage = (String) mv.getModel().get(ERROR_DETAILS_PARAM_NAME);
        assertThat(actualErrorMessage, equalTo(expectedErrorMessage));
    }

    @Test
    public void shouldRejectSubmitIfJSONInvalid() throws Exception {
        DatasetMetadata form = new DatasetMetadata()
                .setJsonMetadata("ASDFGHJKL");

        given(mockBindingResult.hasErrors())
                .willReturn(true);

        MvcResult mvcResult = this.mvc.perform(post("/")
                .param("jsonMetadata", form.getJsonMetadata())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        verify(mockDatasetDAO, never()).createOrUpdateMetadata(any(DatasetMetadata.class));
        assertThat(mvcResult.getModelAndView().getViewName(), equalTo(EDITOR_VIEW));
    }

    private List<String> convertToStringList(Object obj) {
        return (List<String>) obj;
    }

}
