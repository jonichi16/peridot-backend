package com.jonichi.peridot.auth.service;

/**
 * Interface for utility methods related to the user.
 *
 * <p>This interface provides methods for fetching user details based on the current
 * authentication context.</p>
 */
public interface UserUtil {

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @return the id of the authenticated user
     */
    Integer getUserId();
}
