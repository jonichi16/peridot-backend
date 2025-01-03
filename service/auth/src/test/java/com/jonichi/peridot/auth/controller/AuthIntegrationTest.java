package com.jonichi.peridot.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonichi.peridot.auth.config.SecurityConfig;
import com.jonichi.peridot.auth.dto.AuthTokenDTO;
import com.jonichi.peridot.auth.dto.AuthenticateRequestDTO;
import com.jonichi.peridot.auth.dto.RegisterRequestDTO;
import com.jonichi.peridot.auth.repository.UserRepository;
import com.jonichi.peridot.auth.service.AuthService;
import com.jonichi.peridot.common.exception.PeridotDuplicateException;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void register_shouldReturn201Created() throws Exception {
        String username = "test";
        String email = "test@mail.com";
        String password = "secret";

        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        AuthTokenDTO authTokenDTO = AuthTokenDTO.builder()
                .userId(1)
                .accessToken("sampleJwtToken")
                .build();

        when(authService.register(
                registerRequestDTO.username(),
                registerRequestDTO.email(),
                registerRequestDTO.password()
        )).thenReturn(authTokenDTO);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(
                        document("registerSuccess",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    public void register_withInvalidEmail_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ \"username\": \"test\", " +
                "\"email\": \"invalidEmail\", " +
                "\"password\": \"12345\" }";

        // when, then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void register_withMissingFields_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ }";

        // when, then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("invalidRequest",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    public void register_withDuplicateUsername_shouldReturn400BadRequestError() throws Exception {
        // given
        String username = "test";
        String email = "test@mail.com";
        String password = "secret";

        // when
        when(authService.register(username, email, password)).thenThrow(
                new PeridotDuplicateException("Username already exists")
        );
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(username, email, password);

        // then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("duplicateUsername",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    public void register_withError_shouldReturn500InternalServerError() throws Exception {
        // given
        String username = "test";
        String email = "test@mail.com";
        String password = "secret";

        // when
        when(authService.register(username, email, password)).thenThrow(
                new RuntimeException("Something went wrong")
        );
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(username, email, password);

        // then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void authenticate_withWrongCredentials_shouldReturn401UnauthorizedError() throws Exception {
        // given
        String username = "test";
        String password = "secret";

        AuthenticateRequestDTO authenticateRequestDTO = new AuthenticateRequestDTO(username, password);

        // when
        when(authService.authenticate(username, password)).thenThrow(
                new BadCredentialsException("Bad credentials")
        );

        // then
        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticateRequestDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(
                        document("unauthorized",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    public void authenticate_shouldReturn20OK() throws Exception {
        String username = "test";
        String password = "secret";

        AuthenticateRequestDTO authenticateRequestDTO = new AuthenticateRequestDTO(username, password);

        AuthTokenDTO authTokenDTO = AuthTokenDTO.builder()
                .userId(1)
                .accessToken("sampleJwtToken")
                .build();

        when(authService.authenticate(
                authenticateRequestDTO.username(),
                authenticateRequestDTO.password()
        )).thenReturn(authTokenDTO);

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticateRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("authenticate",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );;
    }

    @Test
    public void authenticate_withMissingFields_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ }";

        // when, then
        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("authenticateInvalidRequest",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    public void authenticate_withError_shouldReturn500InternalServerError() throws Exception {
        // given
        String username = "test";
        String password = "secret";

        // when
        when(authService.authenticate(username, password)).thenThrow(
                new RuntimeException("Something went wrong")
        );
        AuthenticateRequestDTO authenticateRequestDTO = new AuthenticateRequestDTO(username, password);

        // then
        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticateRequestDTO)))
                .andExpect(status().isInternalServerError());
    }

}
