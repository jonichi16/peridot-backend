package com.jonichi.peridot.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/**
 * Data Transfer Object representing user and budget details.
 *
 * <p>Includes the user ID and budget ID. Fields with null values are excluded from JSON
 * responses.</p>
 *
 * @param userId   the ID of the user.
 * @param budgetId the ID of the budget.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record UserBudgetDTO(
        Integer userId,
        Integer budgetId
) {
}
