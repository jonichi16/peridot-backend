package com.jonichi.peridot.common.exception;

/**
 * Custom exception class representing a duplicate entity scenario in the Peridot application.
 *
 * <p>The {@code PeridotDuplicateException} is thrown when a conflict occurs due to
 * duplicate entities, such as when attempting to create a user or resource that already exists.</p>
 */
public class PeridotDuplicateException extends RuntimeException {

    /**
     * Constructs a new {@code PeridotDuplicateException} with the specified detail message.
     *
     * @param message the detail message describing the reason for the exception
     */
    public PeridotDuplicateException(String message) {
        super(message);
    }
}
