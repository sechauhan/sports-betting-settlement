package com.sportsbetting.settlement.exception;

/**
 * Constants used for API error responses and exception handling.
 */
public final class ExceptionConstant {

    private ExceptionConstant() {
    }

    // Error codes
    public static final String ERROR_CODE_VALIDATION = "VALIDATION_ERROR";
    public static final String ERROR_CODE_NOT_FOUND = "NOT_FOUND";
    public static final String ERROR_CODE_CONFLICT = "CONFLICT";
    public static final String ERROR_CODE_BAD_REQUEST = "BAD_REQUEST";
    public static final String ERROR_CODE_INTERNAL = "INTERNAL_ERROR";

    // Error messages
    public static final String MESSAGE_VALIDATION_FAILED = "Validation failed";
    public static final String MESSAGE_INVALID_REQUEST_BODY_PREFIX = "Invalid request body: ";
    public static final String MESSAGE_MALFORMED_REQUEST_BODY = "Malformed request body";
    public static final String MESSAGE_MISSING_PARAMETER_PREFIX = "Missing required parameter: ";
    public static final String MESSAGE_UNEXPECTED_ERROR = "An unexpected error occurred. Please try again later.";

    // Message format (use with String.format)
    public static final String FORMAT_INVALID_PARAMETER_VALUE = "Invalid value '%s' for parameter '%s'";
}
