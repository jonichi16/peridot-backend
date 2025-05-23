package com.jonichi.peridot.auth.controller;

import com.jonichi.peridot.auth.dto.AuthTokenDTO;
import com.jonichi.peridot.auth.dto.AuthenticateRequestDTO;
import com.jonichi.peridot.auth.dto.RegisterRequestDTO;
import com.jonichi.peridot.auth.service.AuthService;
import com.jonichi.peridot.common.dto.ApiResponse;
import java.util.Objects;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    @Test
    public void register_shouldCallAuthServiceOnce() throws Exception {
        // given
        String username = "test";
        String email = "test@mail.com";
        String password = "secret";

        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        // when
        authController.register(registerRequestDTO);

        // then
        verify(authService, times(1)).register(
                registerRequestDTO.username(),
                registerRequestDTO.email(),
                registerRequestDTO.password()
        );
    }

    @Test
    public void register_shouldReturnCorrectResponseBody() throws Exception {
        // given
        String username = "test";
        String email = "test@mail.com";
        String password = "secret";

        // when
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
        AuthTokenDTO authTokenDTO = AuthTokenDTO.builder()
                .userId(1)
                .accessToken("jwtToken")
                .build();

        // when
        when(authService.register(username, email, password)).thenReturn(authTokenDTO);
        ResponseEntity<ApiResponse<AuthTokenDTO>> response = authController.register(registerRequestDTO);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(Objects.requireNonNull(response.getBody()).getData().accessToken()).isEqualTo("jwtToken");
        assertThat(Objects.requireNonNull(response.getBody()).getData().userId()).isEqualTo(1);
        assertThat(response.getBody().getMessage()).isEqualTo("User registered successfully");
        assertThat(response.getBody().getCode()).isEqualTo(201);
    }

    @Test
    public void authenticate_shouldCallAuthUseCaseOnce() throws Exception {
        // given
        String username = "test";
        String password = "secret";

        AuthenticateRequestDTO authenticateRequestDTO = new AuthenticateRequestDTO(username, password);

        // when
        authController.authenticate(authenticateRequestDTO);

        // then
        verify(authService, times(1)).authenticate(
                authenticateRequestDTO.username(),
                authenticateRequestDTO.password()
        );
    }

    @Test
    public void authenticate_shouldReturnCorrectResponseBody() throws Exception {
        // given
        String username = "test";
        String password = "secret";

        AuthenticateRequestDTO authenticateRequestDTO = new AuthenticateRequestDTO(username, password);
        AuthTokenDTO authTokenDTO = new AuthTokenDTO(1, "jwtToken");

        // when
        when(authService.authenticate(username, password)).thenReturn(authTokenDTO);
        ResponseEntity<ApiResponse<AuthTokenDTO>> response = authController.authenticate(authenticateRequestDTO);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getData().accessToken()).isEqualTo("jwtToken");
        assertThat(Objects.requireNonNull(response.getBody()).getData().userId()).isEqualTo(1);
        assertThat(response.getBody().getMessage()).isEqualTo("User authenticated successfully");
        assertThat(response.getBody().getCode()).isEqualTo(200);

    }
}
