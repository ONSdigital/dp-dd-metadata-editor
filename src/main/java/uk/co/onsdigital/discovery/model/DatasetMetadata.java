package uk.co.onsdigital.discovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import uk.co.onsdigital.discovery.validation.annotation.*;

/**
 * Model representing the Metadata fields of the DinmensionalDataSet table.
 */
public class DatasetMetadata {

    @JSON(message = "dataset.json.metadata.invalid")
    private String jsonMetadata;

    @NotEmpty(message = "dataset.id.empty")
    @UUID
    private String datasetId;

    @DataResourceID
    private String dataResource;

    @NotEmpty(message = "dataset.major.version.empty")
    private String majorVersion;

    @NotEmpty(message = "dataset.minor.version.empty")
    private String minorVersion;

    private String revisionNotes;
    private String revisionReason;

    public String getRevisionNotes() {
        return revisionNotes;
    }

    public DatasetMetadata setRevisionNotes(String revisionNotes) {
        this.revisionNotes = revisionNotes;
        return this;
    }

    public String getRevisionReason() {
        return revisionReason;
    }

    public DatasetMetadata setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
        return this;
    }

    public String getJsonMetadata() {
        return jsonMetadata;
    }

    public DatasetMetadata setJsonMetadata(String jsonMetadata) {
        this.jsonMetadata = jsonMetadata;
        return this;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public DatasetMetadata setDatasetId(String datasetId) {
        this.datasetId = datasetId;
        return this;
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public DatasetMetadata setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public DatasetMetadata setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
        return this;
    }

    public String getDataResource() {
        return dataResource;
    }

    public DatasetMetadata setDataResource(String dataResource) {
        this.dataResource = dataResource;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DatasetMetadata that = (DatasetMetadata) o;

        return new EqualsBuilder()
                .append(getJsonMetadata(), that.getJsonMetadata())
                .append(getDatasetId(), that.getDatasetId())
                .append(getDataResource(), that.getDataResource())
                .append(getMajorVersion(), that.getMajorVersion())
                .append(getMinorVersion(), that.getMinorVersion())
                .append(getRevisionNotes(), that.getRevisionNotes())
                .append(getRevisionReason(), that.getRevisionReason())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getJsonMetadata())
                .append(getDatasetId())
                .append(getDataResource())
                .append(getMajorVersion())
                .append(getMinorVersion())
                .append(getRevisionNotes())
                .append(getRevisionReason())
                .toHashCode();
    }
}
