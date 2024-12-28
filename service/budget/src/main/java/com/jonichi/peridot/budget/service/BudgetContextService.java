package com.jonichi.peridot.budget.service;

import com.jonichi.peridot.common.dto.UserBudgetDTO;
import java.math.BigDecimal;

/**
 * Service interface for managing the context of a user's budget.
 *
 * <p>This service provides methods related to accessing the current user's budget details.
 * In this case, it retrieves the user's budget ID.</p>
 */
public interface BudgetContextService {

    /**
     * Retrieves the current user's budget ID.
     *
     * <p>Returns a {@link UserBudgetDTO} containing the current user's budget ID.
     * If no budget is found, an exception may be thrown.</p>
     *
     * @return a {@link UserBudgetDTO} containing the current user's budget ID.
     */
    UserBudgetDTO getCurrentUserBudgetId();

    /**
     * Updates the total expenses of a budget.
     *
     * <p>This method updates the total expenses for a specific budget identified
     * by its unique ID.</p>
     *
     * @param budgetId the unique identifier of the budget to update.
     * @param totalExpenses the new total expenses to set for the budget.
     */
    void updateBudgetStatus(Integer budgetId, BigDecimal totalExpenses);
}
