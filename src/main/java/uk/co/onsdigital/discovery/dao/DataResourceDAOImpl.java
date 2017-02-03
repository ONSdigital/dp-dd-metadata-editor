package uk.co.onsdigital.discovery.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import uk.co.onsdigital.discovery.model.DataResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataResourceDAOImpl implements DataResourceDAO {

    static final String CREATE_DATA_RESOURCE_SQL = "INSERT INTO data_resource (data_resource, title) VALUES (:dataResource, :title)";
    static final String QUERY_ALL_DATA_RESOURCES = "SELECT data_resource, title FROM data_resource";

    @Autowired
    private NamedParameterJdbcTemplate bob;

    @Override
    public void createDataResource(DataResource dataResource) {
        Map<String, String> namedParams = new HashMap<>();
        namedParams.put("dataResource", dataResource.getDataResourceID());
        namedParams.put("title", dataResource.getTitle());
        bob.update(CREATE_DATA_RESOURCE_SQL, namedParams);
    }

    @Override
    public List<DataResource> getDataResources() {
        List<DataResource> resources = new ArrayList<>();
        bob.query(QUERY_ALL_DATA_RESOURCES, new HashMap<>(), (rs) -> {
            resources.add(new DataResource()
                    .setTitle(rs.getString("title"))
                    .setDataResourceID(rs.getString("data_resource")));
        });
        return resources;
    }
}
