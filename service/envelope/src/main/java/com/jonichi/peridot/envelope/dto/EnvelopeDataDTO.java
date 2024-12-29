package com.jonichi.peridot.envelope.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jonichi.peridot.envelope.model.BudgetEnvelopeStatus;
import java.math.BigDecimal;
import lombok.Builder;

/**
 * A Data Transfer Object (DTO) representing envelope data.
 *
 * <p>This DTO is used to transfer data related to an envelope, including its name, description,
 * amount, recurring status, and the envelope's current status.</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record EnvelopeDataDTO(
    String name,
    String description,
    BigDecimal amount,
    Boolean recurring,
    BudgetEnvelopeStatus status
) {
}
