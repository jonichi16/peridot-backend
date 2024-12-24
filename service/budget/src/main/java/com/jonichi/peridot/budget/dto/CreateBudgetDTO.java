package com.jonichi.peridot.budget.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) for creating a budget.
 *
 * <p>This DTO is used to encapsulate the data sent in requests for creating a new budget.
 * It includes validation annotations to ensure that the input meets the required constraints.</p>
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record CreateBudgetDTO(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
        @Digits(
                integer = 10,
                fraction = 2,
                message = "Amount must have at most 10 integer digits and 2 fractional digits"
        )
        BigDecimal amount
) {
}
