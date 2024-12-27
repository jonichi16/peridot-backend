package com.jonichi.peridot.envelope.model;

/**
 * Enum representing the possible statuses of a budget envelope.
 *
 * <p>This enum defines the status of a budget envelope, indicating whether it is under the
 * allocated amount, fully allocated, or has exceeded the allocated amount.</p>
 */
public enum BudgetEnvelopeStatus {
    ENVELOPE_STATUS_UNDER,
    ENVELOPE_STATUS_FULL,
    ENVELOPE_STATUS_EXCEEDED
}
