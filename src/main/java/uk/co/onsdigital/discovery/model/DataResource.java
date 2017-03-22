package uk.co.onsdigital.discovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import uk.co.onsdigital.discovery.validation.annotation.JSON;

import javax.validation.constraints.Pattern;

public class DataResource {

    public static final String DATA_RESOURCE_COL_NAME = "data_resource";
    public static final String TITLE_COL_NAME = "title";
    public static final String METADATA_COL_NAME = "metadata";

    @NotEmpty(message = "data.resource.data.resource.id.empty")
    @Pattern(regexp = "[a-zA-Z0-9_\\-]*", message = "data.resource.data.resource.id.regex")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DataResource that = (DataResource) o;

        return new EqualsBuilder()
                .append(getDataResourceID(), that.getDataResourceID())
                .append(getTitle(), that.getTitle())
                .append(getMetadata(), that.getMetadata())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getDataResourceID())
                .append(getTitle())
                .append(getMetadata())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("dataResourceID", dataResourceID)
                .append("title", title)
                .append("metadata", metadata)
                .toString();
    }
}
