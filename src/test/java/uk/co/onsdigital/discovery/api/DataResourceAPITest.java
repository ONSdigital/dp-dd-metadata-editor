package uk.co.onsdigital.discovery.api;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.onsdigital.discovery.dao.DataResourceDAO;
import uk.co.onsdigital.discovery.dao.parameters.NamedParameterFactory;
import uk.co.onsdigital.discovery.exception.UnexpectedErrorException;
import uk.co.onsdigital.discovery.model.CreatedResponse;
import uk.co.onsdigital.discovery.model.DataResource;
import uk.co.onsdigital.discovery.model.ErrorResponse;
import uk.co.onsdigital.discovery.model.ValidationError;
import uk.co.onsdigital.discovery.model.ValidationErrorsResponse;

import java.util.ArrayList;
import java.util.List;

import static java.text.MessageFormat.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.onsdigital.discovery.api.AbstractBaseAPI.LOCATION_LINK;

@RunWith(SpringRunner.class)
@WebMvcTest(DataResourceAPI.class)
public class DataResourceAPITest extends AbstractAPITest {

    private static final String DATA_RESOURCE_ID = "ABCD1234";

    @MockBean
    private DataResourceDAO mockDataResourceDAO;

    @MockBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private NamedParameterFactory namedParameterFactory;

    private DataResourceAPI api;
    private List<DataResource> resources;
    private DataResource dataResource;

    @Before
    public void setup() throws Exception {
        dataResource = new DataResource()
                .setMetadata("{\"Name\":\"Dataset Metadata Test 1\",\"uploadDate\":\"01/01/2017\"}")
                .setTitle("Test Title")
                .setDataResourceID(DATA_RESOURCE_ID);

        resources = new ArrayList<>();
        api = new DataResourceAPI();
    }

    @Test
    public void getAllShouldReturnAllExistingDataResources() throws Exception {
        resources.add(dataResource);

        given(mockDataResourceDAO.getAll())
                .willReturn(resources);

        mvcResult = mvc.perform(get("/dataResources")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        DataResource[] actualEntity = parseJSON(mvcResult, DataResource[].class);

        assertThat(actualEntity, equalTo(Arrays.array(dataResource)));
        verify(mockDataResourceDAO, times(1)).getAll();
        verifyNoMoreInteractions(mockDataResourceDAO);
    }

    @Test
    public void getAllShouldReturnErrorResponseIfDAOThrowsException() throws Exception {
        UnexpectedErrorException errorException = new UnexpectedErrorException(UnexpectedErrorException.ErrorCode.DATABASE_ERROR);
        given(mockDataResourceDAO.getAll())
                .willThrow(errorException);

        mvcResult = mvc.perform(get("/dataResources")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andReturn();

        ErrorResponse expected = new ErrorResponse(errorException, HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse actual = parseJSON(mvcResult, ErrorResponse.class);

        assertThat(actual, equalTo(expected));
        verify(mockDataResourceDAO, times(1)).getAll();
        verifyNoMoreInteractions(mockDataResourceDAO);
    }

    @Test
    public void shouldReturnById() throws Exception {
        given(mockDataResourceDAO.getByID(DATA_RESOURCE_ID))
                .willReturn(dataResource);

        mvcResult = mvc.perform(get("/dataResource/" + DATA_RESOURCE_ID)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        DataResource actual = parseJSON(mvcResult, DataResource.class);

        assertThat(actual, equalTo(dataResource));
        verify(mockDataResourceDAO, times(1)).getByID(DATA_RESOURCE_ID);
        verifyNoMoreInteractions(mockDataResourceDAO);
    }

    @Test
    public void getByIDShouldReturnErrorResponseForDAOException() throws Exception {
        UnexpectedErrorException errorException =
                new UnexpectedErrorException(UnexpectedErrorException.ErrorCode.DATABASE_ERROR);

        given(mockDataResourceDAO.getByID(DATA_RESOURCE_ID))
                .willThrow(errorException);

        mvcResult = mvc.perform(get("/dataResource/" + DATA_RESOURCE_ID)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andReturn();

        ErrorResponse expected = new ErrorResponse(errorException, HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse actual = parseJSON(mvcResult, ErrorResponse.class);

        assertThat(actual, equalTo(expected));
        verify(mockDataResourceDAO, times(1)).getByID(DATA_RESOURCE_ID);
        verifyNoMoreInteractions(mockDataResourceDAO);
    }

    @Test
    public void createShouldReturnSuccess() throws Exception {
        objectMapper.writeValueAsString(dataResource);
        mvcResult = mvc.perform(post("/dataResource")
                .content(objectMapper.writeValueAsString(dataResource))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andReturn();

        String locationURL = "/dataResource/" + DATA_RESOURCE_ID;

        CreatedResponse expected = new CreatedResponse("Success: Saved " +
                format(LOCATION_LINK, locationURL, DATA_RESOURCE_ID));

        assertThat(expected, equalTo(parseJSON(mvcResult, CreatedResponse.class)));
        assertThat(mvcResult.getResponse().getHeader("location"), equalTo(locationURL));
        verify(mockDataResourceDAO, times(1)).create(dataResource);
        verifyNoMoreInteractions(mockDataResourceDAO);
    }

    @Test
    public void createShouldReturnValidationErrorIfInvalid() throws Exception {
        objectMapper.writeValueAsString(dataResource);
        mvcResult = mvc.perform(post("/dataResource")
                .content(objectMapper.writeValueAsString(dataResource.setMetadata("IN VALID JSON")))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();


        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError("data.resource.metadata.invalid.json", "Metadata invalid JSON."));
        ValidationErrorsResponse expected = new ValidationErrorsResponse(errors);

        assertThat(expected, equalTo(parseJSON(mvcResult, ValidationErrorsResponse.class)));
        verifyZeroInteractions(mockDataResourceDAO);
    }

    @Test
    public void createShouldReturnErrorResponseForInternalErrors() throws Exception {
        UnexpectedErrorException unexpectedErrorException =
                new UnexpectedErrorException(UnexpectedErrorException.ErrorCode.DATABASE_ERROR);

        doThrow(unexpectedErrorException)
                .when(mockDataResourceDAO).create(dataResource);

        mvcResult = mvc.perform(post("/dataResource")
                .content(objectMapper.writeValueAsString(dataResource))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andReturn();

        ErrorResponse expected = new ErrorResponse(unexpectedErrorException, HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(expected, equalTo(parseJSON(mvcResult, ErrorResponse.class)));
        verify(mockDataResourceDAO, times(1)).create(dataResource);
        verifyNoMoreInteractions(mockDataResourceDAO);
    }

    @Test
    public void updateShouldReturnValidationErrorResponseIfEmpty() throws Exception {
        // PUT an invalid DataResource object.
        mvcResult = mvc.perform(put("/dataResource/" + DATA_RESOURCE_ID)
                .content(objectMapper.writeValueAsString(dataResource.setDataResourceID("")))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError("data.resource.data.resource.id.empty", "Data Resource ID was empty"));
        ValidationErrorsResponse expected = new ValidationErrorsResponse(errors);

        assertThat(expected, equalTo(parseJSON(mvcResult, ValidationErrorsResponse.class)));
        verifyZeroInteractions(mockDataResourceDAO);
    }

    @Test
    public void updateShouldReturnValidationErrorResponseIfInvalid() throws Exception {
        // PUT an invalid DataResource object.
        mvcResult = mvc.perform(put("/dataResource/" + DATA_RESOURCE_ID)
                .content(objectMapper.writeValueAsString(dataResource.setDataResourceID("invalid:£")))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError("data.resource.data.resource.id.regex", "Data Resource ID must include only letters, numbers, underscore(_) and hyphen (-)"));
        ValidationErrorsResponse expected = new ValidationErrorsResponse(errors);

        ValidationErrorsResponse actual = parseJSON(mvcResult, ValidationErrorsResponse.class);
        assertThat(actual, equalTo(expected));
        verifyZeroInteractions(mockDataResourceDAO);
    }

    @Test
    public void updateShouldReturnErrorForInternalServerErrors() throws Exception {
        UnexpectedErrorException unexpectedErrorException =
                new UnexpectedErrorException(UnexpectedErrorException.ErrorCode.DATABASE_ERROR);

        doThrow(unexpectedErrorException)
                .when(mockDataResourceDAO).update(dataResource);

        mvcResult = mvc.perform(put("/dataResource/" + DATA_RESOURCE_ID)
                .content(objectMapper.writeValueAsString(dataResource))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andReturn();

        ErrorResponse expected = new ErrorResponse(unexpectedErrorException, HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(expected, equalTo(parseJSON(mvcResult, ErrorResponse.class)));
        verify(mockDataResourceDAO, times(1)).update(dataResource);
        verifyNoMoreInteractions(mockDataResourceDAO);
    }

    @Test
    public void shouldUpdateSuccessfully() throws Exception {
        mvcResult = mvc.perform(put("/dataResource/" + DATA_RESOURCE_ID)
                .content(objectMapper.writeValueAsString(dataResource))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andReturn();

        String locationURL = "/dataResource/" + DATA_RESOURCE_ID;

        CreatedResponse expected = new CreatedResponse("Success: Saved " +
                format(LOCATION_LINK, locationURL, DATA_RESOURCE_ID));

        assertThat(expected, equalTo(parseJSON(mvcResult, CreatedResponse.class)));
        assertThat(mvcResult.getResponse().getHeader("location"), equalTo(locationURL));
        verify(mockDataResourceDAO, times(1)).update(dataResource);
        verifyNoMoreInteractions(mockDataResourceDAO);
    }
}
