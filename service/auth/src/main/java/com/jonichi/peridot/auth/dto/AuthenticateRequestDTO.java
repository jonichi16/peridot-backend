package com.jonichi.peridot.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

/**
 * A DTO for user authentication requests.
 *
 * <p>This record encapsulates the username and password submitted by the user during
 * authentication.</p>
 *
 * @param username the username of the user attempting to authenticate
 * @param password the password of the user attempting to authenticate
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record AuthenticateRequestDTO(

        @NotEmpty(message = "Username is required")
        String username,

        @NotEmpty(message = "Password is required")
        String password
) {
}