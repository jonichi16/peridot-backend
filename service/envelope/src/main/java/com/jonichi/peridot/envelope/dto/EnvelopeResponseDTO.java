package com.jonichi.peridot.envelope.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record EnvelopeResponseDTO(
        Integer envelopeId,
        Integer budgetEnvelopeId
) {
}
