package com.jonichi.peridot.budget.model;

/**
 * Enumeration representing the status of a budget.
 *
 * <p>The {@code BudgetStatus} enum defines three possible states for a budget:</p>
 * <ul>
 *     <li>{@link #BUDGET_STATUS_COMPLETE}: Indicates that the budget has been fully utilized or
 *     achieved.</li>
 *     <li>{@link #BUDGET_STATUS_INCOMPLETE}: Indicates that the budget is active but not yet
 *     fully utilized.</li>
 *     <li>{@link #BUDGET_STATUS_INVALID}: Indicates that the budget is no longer valid or has
 *     been flagged as invalid.</li>
 * </ul>
 */
public enum BudgetStatus {
    BUDGET_STATUS_COMPLETE,
    BUDGET_STATUS_INCOMPLETE,
    BUDGET_STATUS_INVALID
}
