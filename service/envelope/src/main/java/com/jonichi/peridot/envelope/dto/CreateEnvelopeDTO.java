package com.jonichi.peridot.envelope.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record CreateEnvelopeDTO(
        @NotEmpty(message = "Name is required")
        String name,

        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.00", message = "Amount must be greater than or equal to 0")
        @Digits(
                integer = 10,
                fraction = 2,
                message = "Amount must have at most 10 integer digits and 2 fractional digits"
        )
        BigDecimal amount,

        @NotNull(message = "Recurring is required")
        Boolean recurring
) {
}
