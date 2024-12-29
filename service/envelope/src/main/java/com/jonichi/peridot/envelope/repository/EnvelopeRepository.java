package com.jonichi.peridot.envelope.repository;

import com.jonichi.peridot.envelope.model.Envelope;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Envelope} entities.
 *
 * <p>This interface extends {@link JpaRepository} to provide CRUD operations for
 * {@link Envelope} entities. It allows interaction with the database for performing
 * operations such as saving, deleting, and querying envelope data.</p>
 */
@Repository
public interface EnvelopeRepository extends JpaRepository<Envelope, Integer> {

    /**
     * Updates the details of an envelope.
     *
     * <p>This method modifies the name, description, and update timestamp of an envelope
     * identified by the given envelope ID.</p>
     *
     * @param envelopeId the unique identifier of the envelope to update.
     * @param name the new name to set for the envelope.
     * @param description the new description to set for the envelope.
     * @return the number of rows affected by the update operation.
     */
    @Transactional
    @Modifying
    @Query("""
            UPDATE Envelope e
            SET
                e.name = :name,
                e.description = :description,
                e.updatedDate = CURRENT_TIMESTAMP
            WHERE e.id = :envelopeId
            """)
    Integer updateEnvelope(
            Integer envelopeId,
            String name,
            String description
    );
}
