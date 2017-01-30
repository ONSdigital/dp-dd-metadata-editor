package uk.co.onsdigital.discovery.controller.exception;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static java.text.MessageFormat.format;

public class MetadataEditorException extends Exception {

    private static final String MSG_FMT = "{0} - {1} : {2}";

    public enum ErrorCode {
        DATABASE_ERROR("Unexpected database error."),

        JSON_PARSE_ERROR("Failed to parse invalid JSON"),

        DATASET_ID_MISSING("DatasetID was null or empty");

        private final String details;

        ErrorCode(String details) {
            this.details = details;
        }
    }

    private ErrorCode errorCode;

    public MetadataEditorException(ErrorCode errorCode) {
        super(errorCode.details);
        this.errorCode = errorCode;
    }

    public MetadataEditorException(ErrorCode errorCode, Exception ex) {
        super(format(MSG_FMT, errorCode, ex.getClass().getSimpleName(), ex.getMessage()));
        this.errorCode = errorCode;
    }

    public String getErrorDetails() {
        return super.getMessage();
    }

    public static String getMsgFmt() {
        return MSG_FMT;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MetadataEditorException that = (MetadataEditorException) o;

        return new EqualsBuilder()
                .append(getErrorCode(), that.getErrorCode())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getErrorCode())
                .toHashCode();
    }
}
