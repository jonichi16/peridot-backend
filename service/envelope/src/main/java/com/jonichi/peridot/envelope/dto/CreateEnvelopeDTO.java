package com.jonichi.peridot.envelope.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;

/**
 * Data Transfer Object for creating a new envelope.
 *
 * <p>Includes fields for the envelope's name, description, amount, and whether it is recurring.
 * Input validation is applied to ensure that required fields are provided and adhere to
 * specific constraints.</p>
 *
 * @param name        the name of the envelope. Must not be empty.
 * @param description an optional description of the envelope.
 * @param amount      the amount for the envelope. Must not be null, must be greater than or equal
 *                    to 0, and must have at most 10 integer digits and 2 fractional digits.
 * @param recurring   indicates if the envelope is recurring. Must not be null.
 */
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
