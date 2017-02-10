package uk.co.onsdigital.discovery.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import uk.co.onsdigital.discovery.dao.parameters.NamedParam;
import uk.co.onsdigital.discovery.dao.parameters.NamedParameterFactory;
import uk.co.onsdigital.discovery.exception.UnexpectedErrorException;
import uk.co.onsdigital.discovery.model.DataResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static uk.co.onsdigital.discovery.exception.UnexpectedErrorException.ErrorCode.DATABASE_ERROR;
import static uk.co.onsdigital.discovery.model.DataResource.DATA_RESOURCE_COL_NAME;
import static uk.co.onsdigital.discovery.model.DataResource.METADATA_COL_NAME;
import static uk.co.onsdigital.discovery.model.DataResource.TITLE_COL_NAME;

@Component
public class DataResourceDAOImpl implements DataResourceDAO {

    static final String CREATE_DATA_RESOURCE_SQL = "INSERT INTO data_resource (data_resource, title, metadata) VALUES (:dataResource, :title, :metadata)";
    static final String UPDATE_DATA_RESOURCE_SQL = "UPDATE data_resource SET title = :title, metadata = :metadata WHERE data_resource = :dataResource";
    static final String QUERY_ALL_DATA_RESOURCES = "SELECT data_resource, title, metadata FROM data_resource";
    static final String QUERY_BY_ID = QUERY_ALL_DATA_RESOURCES + " WHERE data_resource = :dataResource";

    private static final String DATA_RESOURCE_FIELD = "dataResource";
    private static final String TITLE_FIELD = "title";
    private static final String METADATA_FIELD = "metadata";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterFactory namedParameterFactory;

    private RowMapper<DataResource> dataResourceRowMapper = (rs, i) ->
            new DataResource()
                    .setTitle(rs.getString(TITLE_COL_NAME))
                    .setDataResourceID(rs.getString(DATA_RESOURCE_COL_NAME))
                    .setMetadata(rs.getString(METADATA_COL_NAME));

    @Override
    public void create(DataResource dataResource) throws UnexpectedErrorException {
        try {
            NamedParam.ListBuilder builder = new NamedParam.ListBuilder()
                    .addParam(DATA_RESOURCE_FIELD, dataResource.getDataResourceID())
                    .addParam(TITLE_FIELD, dataResource.getTitle())
                    .addParam(METADATA_FIELD, dataResource.getMetadata());
            jdbcTemplate.update(CREATE_DATA_RESOURCE_SQL, namedParameterFactory.create(builder));
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new UnexpectedErrorException(DATABASE_ERROR, ex);
        }
    }

    @Override
    public void update(DataResource dataResource) throws UnexpectedErrorException {
        try {
            NamedParam.ListBuilder builder = new NamedParam.ListBuilder()
                    .addParam(DATA_RESOURCE_FIELD, dataResource.getDataResourceID())
                    .addParam(TITLE_FIELD, dataResource.getTitle())
                    .addParam(METADATA_FIELD, dataResource.getMetadata());
            jdbcTemplate.update(UPDATE_DATA_RESOURCE_SQL, namedParameterFactory.create(builder));
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new UnexpectedErrorException(DATABASE_ERROR, "Error updating Data Resource", ex);
        }
    }

    @Override
    public DataResource getByID(String dataResourceID) throws UnexpectedErrorException {
        try {
            NamedParam.ListBuilder builder = new NamedParam.ListBuilder()
                    .addParam(DATA_RESOURCE_FIELD, dataResourceID);
            return jdbcTemplate.queryForObject(QUERY_BY_ID, namedParameterFactory.create(builder), dataResourceRowMapper);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new UnexpectedErrorException(DATABASE_ERROR, "Error getting all Data Resource by ID", ex);
        }
    }

    @Override
    public List<DataResource> getAll() throws UnexpectedErrorException {
        try {
            List<DataResource> resources = new ArrayList<>();
            jdbcTemplate.query(QUERY_ALL_DATA_RESOURCES, new HashMap<>(), (rs) -> {
                resources.add(new DataResource()
                        .setTitle(rs.getString(TITLE_COL_NAME))
                        .setDataResourceID(rs.getString(DATA_RESOURCE_COL_NAME))
                        .setMetadata(rs.getString(METADATA_COL_NAME)));
            });
            return resources;
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new UnexpectedErrorException(DATABASE_ERROR, "Error getting Data Resources", ex);
        }
    }
}
