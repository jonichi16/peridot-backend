package com.jonichi.peridot.envelope.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jonichi.peridot.envelope.model.BudgetEnvelopeStatus;
import java.math.BigDecimal;
import lombok.Builder;

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
