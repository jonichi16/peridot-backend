package com.jonichi.peridot.auth.service.impl;

import com.jonichi.peridot.auth.repository.UserRepository;
import com.jonichi.peridot.auth.service.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link UserUtil} interface for fetching user details.
 *
 * <p>This class provides the actual logic for retrieving the details of the currently
 * authenticated user from the security context. It uses the {@link UserRepository} to
 * fetch the user based on the authenticated username.</p>
 */
@Service
@RequiredArgsConstructor
public class UserUtilImpl implements UserUtil {

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
