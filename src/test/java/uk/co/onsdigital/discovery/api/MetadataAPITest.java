package uk.co.onsdigital.discovery.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.dao.parameters.NamedParameterFactory;
import uk.co.onsdigital.discovery.exception.BadRequestException;
import uk.co.onsdigital.discovery.model.DatasetMetadata;
import uk.co.onsdigital.discovery.model.ErrorResponse;
import uk.co.onsdigital.discovery.model.ValidationError;
import uk.co.onsdigital.discovery.model.ValidationErrorsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.INVALID_DATASET_UUID;

@RunWith(SpringRunner.class)
@WebMvcTest(MetadataAPI.class)
public class MetadataAPITest {

    @MockBean
    private Model mockModel;

    @MockBean
    private BindingResult mockBindingResult;

    @MockBean
    private DatasetDAO datasetDAOMock;

    @MockBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private NamedParameterFactory namedParameterFactory;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MetadataAPI api;
    private List<DatasetMetadata> metadataList;
    private DatasetMetadata metadata;
    private UUID datasetID;

    @Before
    public void setUp() throws Exception {
        api = new MetadataAPI();
        datasetID = UUID.randomUUID();
        metadataList = new ArrayList<>();
        metadata = new DatasetMetadata()
                .setDataResource("1234")
                .setDatasetId(datasetID.toString())
                .setJsonMetadata("")
                .setMajorVersion("1")
                .setMinorVersion("0");
    }

    @Test
    public void shouldGetAllDatasetMetadata() throws Exception {
        metadataList.add(metadata);

        given(this.datasetDAOMock.getAll())
                .willReturn(metadataList);

        MvcResult mvcResult = this.mvc.perform(get("/metadatas")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        DatasetMetadata[] actualEntity = parseJSON(mvcResult, DatasetMetadata[].class);
        assertThat(actualEntity, equalTo(metadataList.toArray()));
        assertThat(mvcResult.getResponse().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8_VALUE));
        verify(datasetDAOMock, times(1)).getAll();
    }

    @Test
    public void shouldGetDatasetByID() throws Exception {
        given(this.datasetDAOMock.getByDatasetId(datasetID))
                .willReturn(metadata);

        MvcResult mvcResult = this.mvc.perform(get("/metadata/" + datasetID.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        DatasetMetadata actualEntity = parseJSON(mvcResult, DatasetMetadata.class);
        assertThat(actualEntity, equalTo(metadata));
        assertThat(mvcResult.getResponse().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8_VALUE));
        verify(datasetDAOMock, times(1)).getByDatasetId(datasetID);
    }

    @Test
    public void shouldThrowExceptionIfUUIDNotValid() throws Exception {
        MvcResult mvcResult = this.mvc.perform(get("/metadata/123456")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse expectedResponse = new ErrorResponse(new BadRequestException(INVALID_DATASET_UUID.getDetails()), HttpStatus.BAD_REQUEST);

        ErrorResponse actualEntity = parseJSON(mvcResult, ErrorResponse.class);

        assertThat(actualEntity, equalTo(expectedResponse));
        assertThat(mvcResult.getResponse().getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8_VALUE));
        verifyZeroInteractions(datasetDAOMock);
    }

    @Test
    public void shouldCreatedMetadataForValidInput() throws Exception {
        given(namedParameterJdbcTemplate.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .willReturn(1);

        MvcResult mvcResult = this.mvc.perform(put("/metadata/" + datasetID.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(metadata))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(mvcResult.getResponse().getHeader("location"), equalTo("/metadata/" + datasetID.toString()));
        verify(datasetDAOMock, times(1))
                .createOrUpdate(metadata);
    }

    @Test
    public void shouldRejectIfResponseBodyIsInvalid() throws Exception {
        given(namedParameterJdbcTemplate.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .willReturn(1);

        // Set an invalid Json string value.
        metadata.setJsonMetadata("THIS IS NOT A JSON STRING");

        MvcResult mvcResult = this.mvc.perform(put("/metadata/" + datasetID.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(metadata))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError("dataset.json.metadata.invalid", "* Metadata value was not valid JSON."));
        ValidationErrorsResponse expectedResponse = new ValidationErrorsResponse(errors);

        ValidationErrorsResponse actual = parseJSON(mvcResult, ValidationErrorsResponse.class);
        assertThat(actual, equalTo(expectedResponse));
        verifyZeroInteractions(datasetDAOMock);
    }

    private <T> T parseJSON(MvcResult result, Class<T> type) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), type);
    }
}
