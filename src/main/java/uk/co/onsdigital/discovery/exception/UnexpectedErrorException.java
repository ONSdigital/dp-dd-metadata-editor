package uk.co.onsdigital.discovery.exception;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static java.text.MessageFormat.format;

/**
 * Exception type for Metadata Editor errors.
 */
public class UnexpectedErrorException extends Exception {

    private static final String MSG_FMT = "{0} - {1} : {2}";
    private static final String MSG_FMT_2 = "{0}: {1} {2} {3}";

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


        INVALID_DATASET_UUID("DatasetID was not valid UUID"),

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

        public String getDetails() {
            return details;
        }
    }

    private ErrorCode errorCode;

    /**
     * Construct a new {@link UnexpectedErrorException}.
     *
     * @param errorCode {@link ErrorCode} to specify the exception scenario.
     */
    public UnexpectedErrorException(ErrorCode errorCode) {
        super(errorCode.details);
        this.errorCode = errorCode;
    }

    /**
     * Construct a new {@link UnexpectedErrorException}.
     *
     * @param errorCode {@link ErrorCode} to specify the exception scenario.
     * @param ex        the original exception.
     */
    public UnexpectedErrorException(ErrorCode errorCode, Exception ex) {
        super(format(MSG_FMT, errorCode, ex.getClass().getSimpleName(), ex.getMessage()));
        this.errorCode = errorCode;
    }

    public UnexpectedErrorException(ErrorCode errorCode, String details, Exception ex) {
        super(format(MSG_FMT_2, errorCode, details, ex.getClass().getSimpleName(), ex.getMessage()));
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

        UnexpectedErrorException that = (UnexpectedErrorException) o;

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
