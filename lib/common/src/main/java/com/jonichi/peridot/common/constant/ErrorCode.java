package com.jonichi.peridot.common.constant;

/**
 * Constants representing error codes used throughout the application.
 *
 * <p>Each error code corresponds to a specific type of error, making it easier to identify
 * and handle errors consistently in the application. These error codes are typically included
 * in API responses to help clients understand the nature of the error.</p>
 */
public class ErrorCode {
    public static final String NESTED_ERROR = "ERR_001";
    public static final String NOT_FOUND = "ERR_002";
    public static final String DUPLICATE = "ERR_003";
    public static final String MISSING_REQUIRED = "ERR_004";
    public static final String UNAUTHORIZED = "ERR_005";
    public static final String NOT_ALLOWED = "ERR_006";
    public static final String INTERNAL_SERVER_ERROR = "ERR_500";
}
