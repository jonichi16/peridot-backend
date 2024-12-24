package com.jonichi.peridot.auth.service.impl;

import com.jonichi.peridot.auth.dto.AuthTokenDTO;
import com.jonichi.peridot.auth.model.Role;
import com.jonichi.peridot.auth.model.User;
import com.jonichi.peridot.auth.repository.UserRepository;
import com.jonichi.peridot.auth.service.AuthService;
import com.jonichi.peridot.auth.service.JwtService;
import com.jonichi.peridot.common.exception.PeridotDuplicateException;
import com.jonichi.peridot.common.util.TransactionalHandler;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class that implements the user authentication and registration use cases.
 *
 * <p>The {@code AuthServiceImpl} class provides the logic for user registration and
 * authentication. It interacts with the necessary ports for saving users, encoding passwords,
 * authenticating users, and generating JWT tokens for authenticated users.</p>
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticateManager;
    private final TransactionalHandler transactionalHandler;

    @Override
    public AuthTokenDTO register(String username, String email, String password) {
        logger.info("Start - Service - register");

        if (userRepository.findByUsername(username).isPresent()) {
            throw new PeridotDuplicateException("Username already exists");
        }

        String encodedPassword =  passwordEncoder.encode(password);
        User userDetails = User.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER_ROLE_ACCOUNT)
                .build();

        Supplier<User> supplier = () -> userRepository.save(userDetails);

        User user = transactionalHandler.runInTransactionSupplier(supplier);

        logger.info("End - Service - register");
        return AuthTokenDTO.builder()
                .userId(user.getId())
                .accessToken(jwtService.generateToken(user))
                .build();
    }

    @Override
    public AuthTokenDTO authenticate(String username, String password) {
        logger.info("Start - Service - authenticate");

        authenticateManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        User user = userRepository.findByUsername(username).orElseThrow();

        logger.info("End - Service - authenticate");
        return new AuthTokenDTO(user.getId(), jwtService.generateToken(user));
    }
}
