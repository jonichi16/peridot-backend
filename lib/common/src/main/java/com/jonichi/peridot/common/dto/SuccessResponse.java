package com.jonichi.peridot.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A class representing a successful API response structure.
 *
 * <p>The {@code SuccessResponse} class extends {@link ApiResponse} to include data for successful
 * operations. It provides a consistent format for success responses, including HTTP status code,
 * message, and the data payload.</p>
 *
 * @param <T> the type of the data payload in the response
 */
@JsonPropertyOrder({"success", "code", "message", "data", "timestamp"})
public class SuccessResponse<T> extends ApiResponse<T> {

    private final T data;

    /**
     * Constructs a {@code SuccessResponse} with the given HTTP status code, message, and data.
     *
     * @param code the HTTP status code representing the success
     * @param message a descriptive message about the success
     * @param data the data payload of the response
     */
    public SuccessResponse(int code, String message, T data) {
        super(true, code, message);
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public T getError() {
        return null;
    }

    @Override
    public String getErrorCode() {
        return null;
    }

    /**
     * Provides a builder for constructing {@code SuccessResponse} instances.
     *
     * @param <T> the type of the data payload
     * @return a new instance of {@code SuccessResponseBuilder}
     */
    public static <T> SuccessResponseBuilder<T> builder() {
        return new SuccessResponseBuilder<>();
    }

    /**
     * A builder class for constructing {@code SuccessResponse} instances.
     *
     * @param <T> the type of the data payload
     */
    public static class SuccessResponseBuilder<T> {
        private int code;
        private String message;
        private T data;

        /**
         * Sets the HTTP status code for the success response.
         *
         * @param code the HTTP status code
         * @return the current builder instance
         */
        public SuccessResponseBuilder<T> code(int code) {
            this.code = code;
            return this;
        }

        /**
         * Sets the message for the success response.
         *
         * @param message the descriptive success message
         * @return the current builder instance
         */
        public SuccessResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the data payload for the success response.
         *
         * @param data the data payload
         * @return the current builder instance
         */
        public SuccessResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        /**
         * Builds and returns a {@code SuccessResponse} instance.
         *
         * @return a new {@code SuccessResponse} instance with the configured values
         */
        public SuccessResponse<T> build() {
            return new SuccessResponse<>(code, message, data);
        }
    }
}
