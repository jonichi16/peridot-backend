package com.jonichi.peridot.auth.config;

import com.jonichi.peridot.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for Spring Security authentication setup.
 *
 * <p>This class provides the beans required for managing authentication,
 * user details retrieval, password encoding, and authentication providers.</p>
 *
 * <p>It configures Spring Security components to enable secure user authentication
 * and integration with the application's database layer for retrieving user details.</p>
 */
@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final UserRepository userRepository;

    /**
     * Provides a {@link UserDetailsService} bean to fetch user details by username.
     *
     * <p>This method integrates with the {@link UserRepository} to retrieve user details
     * from the database. If a user with the provided username is not found, it throws a
     * {@link UsernameNotFoundException}.</p>
     *
     * @return a {@link UserDetailsService} implementation
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Provides a {@link PasswordEncoder} bean for securely encoding passwords.
     *
     * <p>This bean uses the {@link BCryptPasswordEncoder}, which implements
     * the bcrypt hashing algorithm, to encode and verify user passwords.</p>
     *
     * @return a {@link PasswordEncoder} implementation
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an {@link AuthenticationProvider} bean for authenticating user credentials.
     *
     * <p>This method configures a {@link DaoAuthenticationProvider}, which uses
     * the {@link UserDetailsService} to fetch user details and the {@link PasswordEncoder}
     * to validate credentials.</p>
     *
     * @return an {@link AuthenticationProvider} implementation
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides an {@link AuthenticationManager} bean for managing authentication operations.
     *
     * <p>This method retrieves the {@link AuthenticationManager} from the
     * {@link AuthenticationConfiguration}, which simplifies configuration and
     * setup of the authentication flow.</p>
     *
     * @param config the {@link AuthenticationConfiguration} instance
     * @return an {@link AuthenticationManager} implementation
     * @throws Exception if an error occurs while retrieving the {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
