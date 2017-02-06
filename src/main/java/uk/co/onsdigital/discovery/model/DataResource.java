package uk.co.onsdigital.discovery.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import uk.co.onsdigital.discovery.validation.annotation.JSON;

public class DataResource {

    public static final String DATA_RESOURCE_COL_NAME = "data_resource";
    public static final String TITLE_COL_NAME = "title";
    public static final String METADATA_COL_NAME = "metadata";

    @NotEmpty(message = "data.resource.data.resource.id.empty")
    private String dataResourceID;

    @Length(min = 3, message = "data.resource.title.min.length")
    private String title;

    @JSON(message = "data.resource.metadata.invalid.json")
    private String metadata;

    public DataResource setDataResourceID(String dataResourceID) {
        this.dataResourceID = dataResourceID;
        return this;
    }

    public String getDataResourceID() {
        return dataResourceID;
    }

    public String getTitle() {
        return title;
    }

    public DataResource setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMetadata() {
        return metadata;
    }

    public DataResource setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }
}
