package uk.co.onsdigital.discovery.model;

/**
 * Created by dave on 23/01/2017.
 */
public class DatasetMetadata {

    private String jsonMetadata;
    private String datasetId;
    private String majorVersion;
    private String minorVersion;

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
}
