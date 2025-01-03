package com.jonichi.peridot.auth.service;

import com.jonichi.peridot.auth.model.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * A port for generating JWT tokens.
 *
 * <p>The {@code JwtService} interface defines the contract for generating
 * JSON Web Tokens (JWT) for a user. This token can then be used for user authentication
 * in a secure manner.</p>
 */
public interface JwtService {

    /**
     * Generates a JWT token for the given user.
     *
     * <p>This method takes a {@link User} object and generates a corresponding JWT token
     * that can be used for authentication or authorization purposes.</p>
     *
     * @param user the user for whom the JWT token will be generated
     * @return a JWT token representing the authenticated user
     */
    String generateToken(UserDetails user);

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username from the token
     */
    String extractUsername(String token);

    /**
     * Validates the JWT token by checking the username and expiration.
     *
     * @param token the JWT token
     * @param userDetails the user details to compare against the token
     * @return true if the token is valid, otherwise false
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}
