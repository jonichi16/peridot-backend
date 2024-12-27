package com.jonichi.peridot.envelope.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record CreateEnvelopeDTO(
        String name,
        String description,
        BigDecimal amount,
        boolean recurring
) {
}
