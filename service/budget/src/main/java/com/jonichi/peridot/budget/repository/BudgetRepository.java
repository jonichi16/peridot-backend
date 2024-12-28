package com.jonichi.peridot.budget.repository;

import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.model.BudgetStatus;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Budget} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing built-in methods
 * for standard CRUD operations on the {@code Budget} entity, such as saving, finding,
 * updating, and deleting records.</p>
 *
 */
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    /**
     * Retrieves the current budget for a given user and period.
     *
     * <p>This method fetches the user's budget for the specified period. If a budget exists
     * for the provided user and period, it returns the associated data. If no budget is found,
     * an empty {@link Optional} is returned.</p>
     *
     * @param userId the ID of the user whose budget is to be retrieved
     * @param period the period for which the budget is requested
     * @return an {@link Optional} containing a {@link Budget} with the current budget
     *     details, or an empty {@link Optional} if no budget exists for the given user and period
     */
    @Query("""
            SELECT b
            FROM Budget b
            WHERE b.userId = :userId
                AND b.period = :period
            """)
    Optional<Budget> getCurrentBudget(
            @Param("userId") Integer userId,
            @Param("period") LocalDate period
    );

    /**
     * Updates the budget amount for a specific user and period.
     *
     * <p>This method executes a JPQL query to update the `amount` and `updatedDate` fields
     * of the `Budget` entity for the given `userId` and `period`.
     *
     * @param userId the ID of the user whose budget needs to be updated.
     * @param period the period (e.g., month or specific date) for which the budget is being
     *     updated.
     * @param amount the new budget amount to set.
     */
    @Transactional
    @Modifying
    @Query("""
            UPDATE Budget b
            SET b.amount = :amount, b.updatedDate = CURRENT_TIMESTAMP
            WHERE b.userId = :userId
                AND b.period = :period
            """)
    void updateCurrentBudget(
            @Param("userId") Integer userId,
            @Param("period") LocalDate period,
            @Param("amount") BigDecimal amount
    );

    @Transactional
    @Modifying
    @Query(
            """
            UPDATE Budget b
            SET b.status = :status, b.updatedDate = CURRENT_TIMESTAMP
            WHERE b.id = :budgetId
            """
    )
    void updateBudgetStatus(
            @Param("budgetId") Integer budgetId,
            @Param("status") BudgetStatus status
    );
}
