package com.jonichi.peridot.auth.repository;

import com.jonichi.peridot.auth.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing database operations on {@link User}.
 *
 * <p>The {@code UserRepository} extends Spring Data JPA's {@link JpaRepository},
 * providing CRUD operations and custom query methods for the {@link User}.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     *
     * <p>This method queries the database for a {@link User} with the specified
     * username.</p>
     *
     * @param username the username of the user to find
     * @return an {@link Optional} containing the {@link User} if found, or empty if not
     */
    Optional<User> findByUsername(String username);
}
