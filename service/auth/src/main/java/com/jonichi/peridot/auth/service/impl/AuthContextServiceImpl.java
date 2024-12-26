package com.jonichi.peridot.auth.service.impl;

import com.jonichi.peridot.auth.repository.UserRepository;
import com.jonichi.peridot.auth.service.AuthContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link AuthContextService} interface for fetching user details.
 *
 * <p>This class provides the actual logic for retrieving the details of the currently
 * authenticated user from the security context. It uses the {@link UserRepository} to
 * fetch the user based on the authenticated username.</p>
 */
@Service
@RequiredArgsConstructor
public class AuthContextServiceImpl implements AuthContextService {

    private final UserRepository userRepository;

    @Override
    public Integer getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetails) principal).getUsername();

        return userRepository
                .findByUsername(username)
                .orElseThrow()
                .getId();
    }
}
