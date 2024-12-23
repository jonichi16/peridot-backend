package com.jonichi.peridot.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A class representing an error response structure for API responses.
 *
 * <p>The {@code ErrorResponse} class extends {@link ApiResponse} to include details about
 * errors that occur during API processing. It provides a comprehensive format including
 * an error code, message, and error details. This structure is used to consistently
 * represent error cases across the API.</p>
 *
 * @param <T> the type of the error payload in the response
 */
@JsonPropertyOrder({"success", "code", "message", "errorCode", "error", "timestamp"})
public class ErrorResponse<T> extends ApiResponse<T> {

    private final T error;
    private final String errorCode;

    /**
     * Constructs an {@code ErrorResponse} with the given HTTP status code, message,
     * error code, and error details.
     *
     * @param code the HTTP status code representing the error
     * @param message a descriptive message about the error
     * @param errorCode a code representing the type of error
     * @param error the details of the error payload
     */
    public ErrorResponse(int code, String message, String errorCode, T error) {
        super(false, code, message);
        this.error = error;
        this.errorCode = errorCode;
    }

    @Override
    public T getData() {
        return null;
    }

    @Override
    public T getError() {
        return error;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Provides a builder for constructing {@code ErrorResponse} instances.
     *
     * @param <T> the type of the error payload
     * @return a new instance of {@code ErrorResponseBuilder}
     */
    public static <T> ErrorResponseBuilder<T> builder() {
        return new ErrorResponseBuilder<>();
    }

    /**
     * A builder class for constructing {@code ErrorResponse} instances.
     *
     * @param <T> the type of the error payload
     */
    public static class ErrorResponseBuilder<T> {
        private int code;
        private String message;
        private T error;
        private String errorCode;

        /**
         * Sets the HTTP status code for the error response.
         *
         * @param code the HTTP status code
         * @return the current builder instance
         */
        public ErrorResponseBuilder<T> code(int code) {
            this.code = code;
            return this;
        }

        /**
         * Sets the message for the error response.
         *
         * @param message the descriptive error message
         * @return the current builder instance
         */
        public ErrorResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the error details for the error response.
         *
         * @param error the error details
         * @return the current builder instance
         */
        public ErrorResponseBuilder<T> error(T error) {
            this.error = error;
            return this;
        }

        /**
         * Sets the error code for the error response.
         *
         * @param errorCode the code representing the type of error
         * @return the current builder instance
         */
        public ErrorResponseBuilder<T> errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        /**
         * Builds and returns an {@code ErrorResponse} instance.
         *
         * @return a new {@code ErrorResponse} instance with the configured values
         */
        public ErrorResponse<T> build() {
            return new ErrorResponse<>(code, message, errorCode, error);
        }
    }
}
