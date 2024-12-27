package com.jonichi.peridot.envelope.repository;

import com.jonichi.peridot.envelope.model.Envelope;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Envelope} entities.
 *
 * <p>This interface extends {@link JpaRepository} to provide CRUD operations for
 * {@link Envelope} entities. It allows interaction with the database for performing
 * operations such as saving, deleting, and querying envelope data.</p>
 */
public interface EnvelopeRepository extends JpaRepository<Envelope, Integer> {
}
