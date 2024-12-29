package com.jonichi.peridot.envelope.repository;

import com.jonichi.peridot.envelope.model.BudgetEnvelope;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /**
     * Updates the details of a budget envelope.
     *
     * <p>This method modifies the amount, recurring status, and update timestamp of a
     * budget envelope identified by the given budget envelope ID.</p>
     *
     * @param budgetEnvelopeId the unique identifier of the budget envelope to update.
     * @param amount the new amount to set for the budget envelope.
     * @param recurring the new recurring status to set for the budget envelope.
     * @return the number of rows affected by the update operation.
     */
    @Transactional
    @Modifying
    @Query("""
            UPDATE BudgetEnvelope be
            SET
                be.amount = :amount,
                be.recurring = :recurring,
                be.updatedDate = CURRENT_TIMESTAMP
            WHERE be.id = :budgetEnvelopeId
            """)
    Integer updateBudgetEnvelope(
            @Param("budgetEnvelopeId") Integer budgetEnvelopeId,
            @Param("amount") BigDecimal amount,
            @Param("recurring") Boolean recurring
    );
}
