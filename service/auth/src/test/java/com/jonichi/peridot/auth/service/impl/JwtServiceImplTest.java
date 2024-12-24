package com.jonichi.peridot.auth.service.impl;

import com.jonichi.peridot.auth.model.Role;
import com.jonichi.peridot.auth.model.User;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtServiceImplTest {

    private JwtServiceImpl jwtServiceImpl;

    @BeforeEach
    void setUp() {
        jwtServiceImpl = new JwtServiceImpl();
        // Test secret key
        jwtServiceImpl.secretKey = "r+uZcNlc4WPU5ilIFqkJTLVcEtPb3" +
                "hs7HY3gF0Ix4lo9zkr8RCgJ5c1UqQavbvfY" +
                "TfobHix3dK3mhIlh3ayHJg==";

        // 1 minute test token expiration
        jwtServiceImpl.tokenExpiration = 1000 * 60;
    }

    @Test
    public void generateToken_shouldGenerateWithUser() throws Exception {
        // given
        User user = User.builder()
                .id(1)
                .username("test")
                .email("test@mail.com")
                .password("secret")
                .role(Role.USER)
                .build();

        // when
        String token = jwtServiceImpl.generateToken(user);

        // then
        assertThat(token).isNotNull();
        assertThat(jwtServiceImpl.extractUsername(token)).isEqualTo("test");
    }

    @Test
    public void generateToken_withExtraClaims() throws Exception {
        // given
        User user = User.builder()
                .id(1)
                .username("test")
                .email("test@mail.com")
                .password("secret")
                .role(Role.USER)
                .build();

        // when
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());

        // then
        String token = jwtServiceImpl.generateToken(extraClaims, user);

        assertThat(token).isNotNull();
        assertThat(jwtServiceImpl.extractUsername(token)).isEqualTo("test");
    }

    @Test
    public void isTokenExpired() throws Exception {
        // given
        User user = User.builder()
                .id(1)
                .username("test")
                .email("test@mail.com")
                .password("secret")
                .role(Role.USER)
                .build();

        String token = jwtServiceImpl.generateToken(user);
        assertThat(jwtServiceImpl.isTokenExpired(token)).isFalse();

        jwtServiceImpl.tokenExpiration = 0; // update expiration time
        String expiredToken = jwtServiceImpl.generateToken(user);

        assertThat(jwtServiceImpl.isTokenExpired(expiredToken)).isTrue();
    }
    
    @Test
    public void isTokenValid() throws Exception {
        // given
        User user = User.builder()
                .id(1)
                .username("test")
                .email("test@mail.com")
                .password("secret")
                .role(Role.USER)
                .build();

        String token = jwtServiceImpl.generateToken(user);

        assertThat(jwtServiceImpl.isTokenValid(token, user)).isTrue();

        User user2 = User.builder()
                .id(2)
                .username("test2")
                .email("test2@mail.com")
                .password("secret")
                .role(Role.USER)
                .build();

        assertThat(jwtServiceImpl.isTokenValid(token, user2)).isFalse();
        jwtServiceImpl.tokenExpiration = 0; // update expiration time
        String expiredToken = jwtServiceImpl.generateToken(user2);

        assertThat(jwtServiceImpl.isTokenValid(expiredToken, user2)).isFalse();

    }

}
