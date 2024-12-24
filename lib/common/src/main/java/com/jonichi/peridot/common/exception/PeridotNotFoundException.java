package com.jonichi.peridot.common.exception;

/**
 * Custom exception to indicate that a resource was not found.
 *
 * <p>This exception is thrown when a requested resource could not be found. It extends
 * {@link RuntimeException} and provides a custom message to describe the error.</p>
 */
public class PeridotNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code PeridotNotFoundException} with the specified detail message.
     *
     * @param message the detail message, which provides more information about the error
     */
    public PeridotNotFoundException(String message) {
        super(message);
    }
}
