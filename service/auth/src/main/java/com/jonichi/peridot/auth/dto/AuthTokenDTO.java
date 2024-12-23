package com.jonichi.peridot.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/**
 * A Data Transfer Object (DTO) for representing authentication tokens.
 *
 * <p>This DTO encapsulates the details of an authentication response, including the token string
 * and the user ID associated with the token. It is used to provide a consistent structure for
 * authentication-related data in API responses.</p>
 *
 * <p>Fields that are {@code null} will be excluded from the JSON serialization due to the
 * {@link JsonInclude} annotation.</p>
 *
 * @param userId the unique identifier of the user
 * @param accessToken the authentication token string
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record AuthTokenDTO(
        Integer userId,
        String accessToken
) {
}