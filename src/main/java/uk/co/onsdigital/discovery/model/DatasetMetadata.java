package uk.co.onsdigital.discovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model representing the Metadata fields of the DinmensionalDataSet table.
 */
public class DatasetMetadata {

    private String jsonMetadata;
    private String datasetId;
    private String majorVersion;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DatasetMetadata metadata = (DatasetMetadata) o;

        return new EqualsBuilder()
                .append(getJsonMetadata(), metadata.getJsonMetadata())
                .append(getDatasetId(), metadata.getDatasetId())
                .append(getMajorVersion(), metadata.getMajorVersion())
                .append(getMinorVersion(), metadata.getMinorVersion())
                .append(getRevisionNotes(), metadata.getRevisionNotes())
                .append(getRevisionReason(), metadata.getRevisionReason())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getJsonMetadata())
                .append(getDatasetId())
                .append(getMajorVersion())
                .append(getMinorVersion())
                .append(getRevisionNotes())
                .append(getRevisionReason())
                .toHashCode();
    }
}
