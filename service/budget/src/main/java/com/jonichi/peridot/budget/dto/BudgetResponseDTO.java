package com.jonichi.peridot.budget.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record BudgetResponseDTO(
        Integer budgetId
) {
}
