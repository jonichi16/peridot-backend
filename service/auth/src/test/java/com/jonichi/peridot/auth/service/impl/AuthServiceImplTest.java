package com.jonichi.peridot.auth.service.impl;

import com.jonichi.peridot.auth.dto.AuthTokenDTO;
import com.jonichi.peridot.auth.model.Role;
import com.jonichi.peridot.auth.model.User;
import com.jonichi.peridot.auth.repository.UserRepository;
import com.jonichi.peridot.auth.service.JwtService;
import com.jonichi.peridot.common.exception.PeridotDuplicateException;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticateManager;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void register_withUserDetails_shouldReturnToken() throws Exception {
        // given
        String username = "test";
        String email = "test@mail.com";
        String password = "secret";
        String encodedPassword = "encodedPassword";

        User request = User.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        User user = User.builder()
                .id(2)
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        // when
        when(userRepository.save(request)).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        AuthTokenDTO authTokenDTO = authService.register(username, email, password);

        // then
        verify(passwordEncoder, times(1)).encode(password);
        verify(jwtService, times(1)).generateToken(user);
        assertThat(authTokenDTO.accessToken()).isEqualTo("jwtToken");
        assertThat(authTokenDTO.userId()).isEqualTo(2);
    }

    @Test
    public void register_shouldCheckForExistingUser() throws Exception {
        // given
        String username = "test";
        String email = "test@mail.com";
        String password = "secret";
        String encodedPassword = "encodedPassword";

        User request = User.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        User user = User.builder()
                .id(2)
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        // when
        when(userRepository.save(request)).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        // then
        assertThatNoException().isThrownBy(() ->
                authService.register(username, email, password));
        verify(userRepository, times(1)).findByUsername(username);

    }

    @Test
    public void register_withDuplicate_shouldThrowPeridotDuplicateException() throws Exception {
        // given
        String username = "test";
        String email = "test@mail.com";
        String password = "secret";
        String encodedPassword = "encodedPassword";

        User user = User.builder()
                .id(2)
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        // when
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        // then
        assertThatThrownBy(() -> authService.register(username, email, password))
                .isInstanceOf(PeridotDuplicateException.class)
                .hasMessage("Username already exists");
    }

    @Test
    public void authenticate_shouldReturnJwtToken() throws Exception {
        // given
        String username = "test";
        String email = "test@mail.com";
        String password = "secret";
        String encodedPassword = "encodedPassword";

        User user = User.builder()
                .id(2)
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        // when
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        AuthTokenDTO authTokenDTO = new AuthTokenDTO(2, "jwtToken");

        // then
        assertThat(authService.authenticate(username, password)).isEqualTo(authTokenDTO);
        verify(authenticateManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

}
