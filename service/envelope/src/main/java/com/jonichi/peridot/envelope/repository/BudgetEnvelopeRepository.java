package com.jonichi.peridot.envelope.repository;

import com.jonichi.peridot.envelope.model.BudgetEnvelope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetEnvelopeRepository extends JpaRepository<BudgetEnvelope, Integer> {
}
