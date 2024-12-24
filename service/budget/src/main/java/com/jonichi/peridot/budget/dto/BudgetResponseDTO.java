package com.jonichi.peridot.budget.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) for representing a budget response.
 *
 * <p>This DTO is used to encapsulate the data returned in the response
 * when a budget is created or retrieved. It contains the ID of the created or existing budget.</p>
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record BudgetResponseDTO(
        Integer budgetId
) {
}
