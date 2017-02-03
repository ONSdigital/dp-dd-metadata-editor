package uk.co.onsdigital.discovery.model;

import org.hibernate.validator.constraints.NotEmpty;
import uk.co.onsdigital.discovery.validation.JSONString;

public class DataResource {

    @NotEmpty(message = "data.resource.data.resource.id.empty")
    private String dataResourceID;

    @NotEmpty(message = "data.resource.title.empty")
    private String title;

    @JSONString(message = "data.resource.metadata.invalid.json")
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
