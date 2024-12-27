package com.jonichi.peridot.envelope.service;

import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import java.math.BigDecimal;

/**
 * Service interface for managing envelopes.
 *
 * <p>Provides operations for creating and managing envelopes, including methods to handle
 * envelope creation and other related functionalities.</p>
 */
public interface EnvelopeService {

    /**
     * Creates a new envelope.
     *
     * <p>This method creates a new envelope with the provided name, description, amount,
     * and recurrence status. It returns the details of the created envelope.</p>
     *
     * @param name        the name of the envelope. Must not be null or empty.
     * @param description an optional description of the envelope.
     * @param amount      the amount allocated to the envelope. Must be greater than or equal to 0.
     * @param recurring   indicates whether the envelope is recurring.
     * @return a {@link EnvelopeResponseDTO} containing the details of the created envelope.
     */
    EnvelopeResponseDTO createEnvelope(
            String name,
            String description,
            BigDecimal amount,
            Boolean recurring
    );
}
