package com.jonichi.peridot.envelope.repository;

import com.jonichi.peridot.envelope.model.Envelope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvelopeRepository extends JpaRepository<Envelope, Integer> {
}
