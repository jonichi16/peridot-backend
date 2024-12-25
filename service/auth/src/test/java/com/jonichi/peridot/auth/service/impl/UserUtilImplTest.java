package com.jonichi.peridot.auth.service.impl;

import com.jonichi.peridot.auth.model.Role;
import com.jonichi.peridot.auth.model.User;
import com.jonichi.peridot.auth.repository.UserRepository;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserUtilImplTest {

    @Mock
    private Authentication auth;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetails userDetails;

    private UserUtilImpl userUtil;

    @BeforeEach
    public void initSecurityContext() {
        userUtil = new UserUtilImpl(userRepository);
        when(auth.getPrincipal()).thenReturn("test");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getUserId_shouldReturnUserId() throws Exception {
        // given
        User existingUser = User.builder()
                .id(2)
                .username("test")
                .email("test@mail.com")
                .password("encodedPassword")
                .role(Role.USER_ROLE_ACCOUNT)
                .build();

        // when
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test");
        when(userRepository.findByUsername("test")).thenReturn(
                Optional.of(existingUser)
        );
        Integer userId = userUtil.getUserId();

        // then
        verify(userRepository, times(1)).findByUsername("test");
        assertThat(userId).isEqualTo(existingUser.getId());
    }
}
