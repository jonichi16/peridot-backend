package com.jonichi.peridot.budget.repository;

import com.jonichi.peridot.budget.dto.BudgetDataDTO;
import com.jonichi.peridot.budget.model.Budget;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Budget} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing built-in methods
 * for standard CRUD operations on the {@code Budget} entity, such as saving, finding,
 * updating, and deleting records.</p>
 *
 */
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
     * @return an {@link Optional} containing a {@link BudgetDataDTO} with the current budget
     *     details, or an empty {@link Optional} if no budget exists for the given user and period
     */
    Optional<BudgetDataDTO> getCurrentBudget(Integer userId, LocalDate period);
}
