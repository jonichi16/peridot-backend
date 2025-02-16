package com.jonichi.peridot.budget.service;

import com.jonichi.peridot.budget.dto.BudgetDataDTO;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.common.exception.PeridotNotFoundException;
import java.math.BigDecimal;

/**
 * Interface defining the contract for budget-related operations.
 *
 * <p>Provides methods to manage budget creation and retrieval. Implementations of this
 * interface are responsible for handling the business logic and interacting with the
 * persistence layer.</p>
 */
public interface BudgetService {

    /**
     * Creates a new budget with the specified amount.
     *
     * <p>This method validates the input amount, associates the budget with the currently
     * authenticated user, and persists it to the database. The created budget is returned as a
     * DTO.</p>
     *
     * @param amount the amount to set for the new budget (must be positive and non-null)
     * @return a {@link BudgetResponseDTO} containing details of the created budget
     */
    BudgetResponseDTO createBudget(BigDecimal amount);
    
    /**
     * Retrieves the current budget for the user.
     *
     * <p>This method fetches the budget information for the current period,
     * including the amount and status of the budget.</p>
     *
     * @return a {@link BudgetDataDTO} containing the current budget's period,
     *         amount, and status.
     */
    BudgetDataDTO getCurrentBudget();

    /**
     * Updates the current budget amount for the authenticated user.
     *
     * <p>This method retrieves the current user's ID and period, updates the budget amount
     * in a transactional context, and fetches the updated budget details to return as a DTO.
     * If the budget does not exist for the given user and period, an exception is thrown.
     *
     * @param amount the new budget amount to set.
     * @return a {@link BudgetResponseDTO} containing details of the updated budget.
     * @throws PeridotNotFoundException if the budget does not exist for the given user and period.
     *
     */
    BudgetResponseDTO updateCurrentBudget(BigDecimal amount);
}
