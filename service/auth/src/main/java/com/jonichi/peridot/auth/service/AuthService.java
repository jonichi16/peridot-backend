package com.jonichi.peridot.auth.service;

import com.jonichi.peridot.auth.dto.AuthTokenDTO;

/**
 * Auth Service interface for authentication-related operations.
 *
 * <p>This interface defines the contract for user authentication and registration processes.
 * Implementations of this interface should handle the business logic for user registration
 * and authentication, returning the appropriate authentication token data.</p>
 */
public interface AuthService {

    /**
     * Registers a new user with the provided details.
     *
     * <p>This method creates a new user account with the specified username, email, and password.
     * Upon successful registration, it generates and returns an authentication token.</p>
     *
     * @param username the desired username for the new user
     * @param email the email address of the new user
     * @param password the password for the new user account
     * @return an {@link AuthTokenDTO} containing the authentication token and user details
     */
    AuthTokenDTO register(String username, String email, String password);
}
