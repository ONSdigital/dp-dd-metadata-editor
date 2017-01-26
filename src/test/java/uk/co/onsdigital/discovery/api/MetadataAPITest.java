package uk.co.onsdigital.discovery.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.model.DatasetMetadata;

import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by dave on 26/01/2017.
 */
@RunWith(SpringRunner.class)
@RestClientTest(MetadataAPI.class)
public class MetadataAPITest {

    @Autowired
    private MetadataAPI metadataAPI;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private DatasetDAO datasetDAOMock;

    private MockRestServiceServer server;

    @Before
    public void setup() {
        server = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    public void testExample() throws Exception {
        UUID uuid = UUID.randomUUID();
        DatasetMetadata metadata = new DatasetMetadata().setDatasetId(uuid.toString());

        String json = new ObjectMapper().writeValueAsString(metadata);

        this.server.expect(requestTo("/metadata/" + uuid.toString()))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        this.metadataAPI.getMetaData(uuid.toString());
    }

}
