package uk.co.onsdigital.discovery.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;

public abstract class AbstractAPITest {

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected Model mockModel;

    @MockBean
    protected BindingResult mockBindingResult;

    @Autowired
    protected MockMvc mvc;

    protected MvcResult mvcResult;

    protected <T> T parseJSON(MvcResult result, Class<T> type) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), type);
    }
}
