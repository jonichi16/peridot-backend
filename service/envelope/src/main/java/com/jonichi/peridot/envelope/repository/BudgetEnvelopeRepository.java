package com.jonichi.peridot.envelope.repository;

import com.jonichi.peridot.envelope.model.BudgetEnvelope;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link BudgetEnvelope} entities.
 *
 * <p>This interface extends {@link JpaRepository} to provide CRUD operations for
 * {@link BudgetEnvelope} entities. It allows interaction with the database for performing
 * operations such as saving, deleting, and querying budget envelope data.</p>
 */
@Repository
public interface BudgetEnvelopeRepository extends JpaRepository<BudgetEnvelope, Integer> {

    /**
     * Retrieves the total expenses for a specific budget.
     *
     * <p>This method calculates the sum of all amounts in the {@code BudgetEnvelope}
     * entities associated with the given budget ID.</p>
     *
     * @param budgetId the unique identifier of the budget.
     * @return the total expenses as a {@code BigDecimal}.
     */
    @Query("""
            SELECT SUM(be.amount)
            FROM BudgetEnvelope be
            WHERE be.budgetId = :budgetId
            """)
    BigDecimal getTotalExpenses(
            @Param("budgetId") Integer budgetId
    );

    @Query("""
            SELECT 1
            """)
    Integer updateEnvelope(
            Integer budgetEnvelopeId,
            String name,
            String description,
            BigDecimal amount,
            Boolean recurring
    );
}
