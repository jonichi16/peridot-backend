package com.jonichi.peridot.envelope.service;

import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import java.math.BigDecimal;

public interface EnvelopeService {
    EnvelopeResponseDTO createEnvelope(
            String name,
            String description,
            BigDecimal amount,
            boolean recurring
    );
}
