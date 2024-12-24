package com.jonichi.peridot.budget.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jonichi.peridot.budget.model.BudgetStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

/**
 * Data transfer object (DTO) representing budget data for a specific period.
 *
 * <p>This DTO is used to transfer budget information, including the budget period, amount, and
 * status.</p>
 *
 * @param period the budget period, represented as a {@link LocalDate}
 * @param amount the amount allocated for the budget, represented as a {@link BigDecimal}
 * @param status the current status of the budget, represented as a {@link BudgetStatus}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record BudgetDataDTO(
        LocalDate period,
        BigDecimal amount,
        BudgetStatus status
) {
}
