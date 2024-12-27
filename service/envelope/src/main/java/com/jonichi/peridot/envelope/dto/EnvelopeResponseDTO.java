package com.jonichi.peridot.envelope.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/**
 * Data Transfer Object representing the response for an envelope.
 *
 * <p>Includes the envelope ID and the associated budget envelope ID. Fields with null values
 * are excluded from JSON responses.</p>
 *
 * @param envelopeId      the ID of the envelope.
 * @param budgetEnvelopeId the ID of the associated budget envelope.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record EnvelopeResponseDTO(
        Integer envelopeId,
        Integer budgetEnvelopeId
) {
}
