package com.jonichi.peridot.envelope.repository;

import com.jonichi.peridot.envelope.model.BudgetEnvelope;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link BudgetEnvelope} entities.
 *
 * <p>This interface extends {@link JpaRepository} to provide CRUD operations for
 * {@link BudgetEnvelope} entities. It allows interaction with the database for performing
 * operations such as saving, deleting, and querying budget envelope data.</p>
 */
public interface BudgetEnvelopeRepository extends JpaRepository<BudgetEnvelope, Integer> {
}
