package com.jonichi.peridot.envelope.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "budget_envelope")
public class BudgetEnvelope {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "budget_envelope_sequence")
    @SequenceGenerator(
            name = "budget_envelope_sequence",
            sequenceName = "budget_envelope_sequence",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "budget_id", nullable = false)
    private Integer budgetId;

    @Column(name = "envelope_id", nullable = false)
    private Integer envelopeId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Boolean recurring;

    @Enumerated(EnumType.STRING)
    private BudgetEnvelopeStatus status;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}
