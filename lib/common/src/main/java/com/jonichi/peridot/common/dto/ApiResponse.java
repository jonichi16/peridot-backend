package com.jonichi.peridot.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import lombok.Getter;

/**
 * Abstract base class representing a standardized API response structure.
 *
 * <p>The {@code ApiResponse} class provides a common format for API responses, encapsulating
 * details such as success status, HTTP status code, message, timestamp, and data or error
 * information. It uses Jackson annotations for JSON serialization.</p>
 *
 * <p>The subclasses of {@code ApiResponse} must implement methods to provide either the
 * response data or error details, ensuring flexibility in representing different API outcomes.</p>
 *
 * @param <T> the type of the data or error payload in the response
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ApiResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
    private final String timestamp;

    /**
     * Constructs an {@code ApiResponse} with the given success status, HTTP status code, and
     * message. The timestamp is automatically generated at the time of instantiation.
     *
     * @param success the success status of the API response
     * @param code the HTTP status code representing the API response status
     * @param message a descriptive message about the API response
     */
    protected ApiResponse(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.timestamp = Instant.now().toString();
    }

    /**
     * Returns the data payload of the API response.
     *
     * @return the data payload, or {@code null} if not applicable
     */
    public abstract T getData();

    /**
     * Returns the error details of the API response.
     *
     * @return the error details, or {@code null} if not applicable
     */
    public abstract T getError();

    /**
     * Returns the error code associated with the API response, if any.
     *
     * @return the error code, or {@code null} if not applicable
     */
    public abstract String getErrorCode();
}