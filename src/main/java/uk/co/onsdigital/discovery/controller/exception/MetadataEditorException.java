package uk.co.onsdigital.discovery.controller.exception;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static java.text.MessageFormat.format;

/**
 * Exception type for Metadata Editor errors.
 */
public class MetadataEditorException extends Exception {

    private static final String MSG_FMT = "{0} - {1} : {2}";

    /**
     * Types for the different exception scenarios when calling the Editor.
     */
    public enum ErrorCode {
        /**
         * Unexpected error while executing an SQL querying.
         */
        DATABASE_ERROR("Unexpected database error."),

        /**
         * JSON parsing validation.
         */
        JSON_PARSE_ERROR("Failed to parse invalid JSON"),

        /**
         * Empty of NULL dataset ID.
         */
        DATASET_ID_MISSING("DatasetID was null or empty"),

        /**
         * For {@link NumberFormatException} when dealing with major/minor versions.
         */
        STRING_TO_INT_ERROR("Failed parsing String to int."),

        /**
         * Generic 'something went wrong' error.
         */
        UNEXPECTED_ERROR("An unexpected error occurred");

        private final String details;

        ErrorCode(String details) {
            this.details = details;
        }
    }

    private ErrorCode errorCode;

    /**
     * Construct a new {@link MetadataEditorException}.
     *
     * @param errorCode {@link ErrorCode} to specify the exception scenario.
     */
    public MetadataEditorException(ErrorCode errorCode) {
        super(errorCode.details);
        this.errorCode = errorCode;
    }

    /**
     * Construct a new {@link MetadataEditorException}.
     *
     * @param errorCode {@link ErrorCode} to specify the exception scenario.
     * @param ex        the original exception.
     */
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
