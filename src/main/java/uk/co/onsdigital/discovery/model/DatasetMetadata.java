package uk.co.onsdigital.discovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import uk.co.onsdigital.discovery.validation.annotation.DataResourceID;
import uk.co.onsdigital.discovery.validation.annotation.JSON;
import uk.co.onsdigital.discovery.validation.annotation.UUID;

import javax.validation.constraints.NotNull;

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

    @NotNull(message = "dataset.major.version.empty")
    private Integer majorVersion;

    @NotEmpty(message = "dataset.major.label.empty")
    private String majorLabel;

    @NotNull(message = "dataset.minor.version.empty")
    private Integer minorVersion;
    private String revisionNotes;
    private String revisionReason;

    @NotEmpty (message = "dataset.title.empty")
    private String title;

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

    public Integer getMajorVersion() {
        return majorVersion;
    }

    public DatasetMetadata setMajorVersion(Integer majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }

    public DatasetMetadata setMinorVersion(Integer minorVersion) {
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

    public String getMajorLabel() {
        return majorLabel;
    }

    public DatasetMetadata setMajorLabel(String majorLabel) {
        this.majorLabel = majorLabel;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DatasetMetadata setTitle(String title) {
        this.title = title;
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
                .append(getMajorLabel(), that.getMajorLabel())
                .append(getTitle(), that.getTitle())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getJsonMetadata())
                .append(getDatasetId())
                .append(getDataResource())
                .append(getMajorVersion())
                .append(getMajorLabel())
                .append(getMinorVersion())
                .append(getRevisionNotes())
                .append(getRevisionReason())
                .append(getTitle())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("jsonMetadata", jsonMetadata)
                .append("datasetId", datasetId)
                .append("dataResource", dataResource)
                .append("majorVersion", majorVersion)
                .append("majorLabel", majorLabel)
                .append("minorVersion", minorVersion)
                .append("revisionNotes", revisionNotes)
                .append("revisionReason", revisionReason)
                .append("title", title)
                .toString();
    }
}
