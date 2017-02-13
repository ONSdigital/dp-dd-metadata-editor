package uk.co.onsdigital.discovery.controller;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(MetadataController.class)
public class MetadataControllerTest {
/*
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

        ModelAndView modelAndView = this.mvc.perform(get("/metadata")
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

        MvcResult mvcResult = this.mvc.perform(get("/metadata")
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

        MvcResult mvcResult = this.mvc.perform(post("/metadata")
                .param("jsonMetadata", form.getJsonMetadata())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        verify(mockDatasetDAO, never()).createOrUpdate(any(DatasetMetadata.class));
        assertThat(mvcResult.getModelAndView().getViewName(), equalTo(EDITOR_VIEW));
    }

    private List<String> convertToStringList(Object obj) {
        return (List<String>) obj;
    }*/

}
