package com.jonichi.peridot.envelope.service.impl;

import com.jonichi.peridot.budget.service.BudgetContextService;
import com.jonichi.peridot.common.dto.UserBudgetDTO;
import com.jonichi.peridot.common.exception.PeridotDuplicateException;
import com.jonichi.peridot.common.model.SystemStatus;
import com.jonichi.peridot.common.util.TransactionalHandler;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.model.BudgetEnvelope;
import com.jonichi.peridot.envelope.model.BudgetEnvelopeStatus;
import com.jonichi.peridot.envelope.model.Envelope;
import com.jonichi.peridot.envelope.repository.BudgetEnvelopeRepository;
import com.jonichi.peridot.envelope.repository.EnvelopeRepository;
import com.jonichi.peridot.envelope.service.EnvelopeService;
import java.math.BigDecimal;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link EnvelopeService} interface for managing envelopes.
 *
 * <p>This service handles the creation of envelopes and associated budget envelopes. It
 * interacts with the repository layer to persist envelope and budget envelope details in
 * the database. A transaction is used to ensure that the operations are atomic.</p>
 */
@Service
@RequiredArgsConstructor
public class EnvelopeServiceImpl implements EnvelopeService {

    private static final Logger logger = LoggerFactory.getLogger(EnvelopeServiceImpl.class);
    private final BudgetContextService budgetContextService;
    private final EnvelopeRepository envelopeRepository;
    private final BudgetEnvelopeRepository budgetEnvelopeRepository;
    private final TransactionalHandler transactionalHandler;

    @Override
    public EnvelopeResponseDTO createEnvelope(
            String name,
            String description,
            BigDecimal amount,
            Boolean recurring
    ) {
        logger.info("Start - Service - createEnvelope");

        try {
            UserBudgetDTO userBudgetDTO = budgetContextService.getCurrentUserBudgetId();

            Supplier<EnvelopeResponseDTO> supplier = () -> {
                Envelope envelope = envelopeRepository.save(Envelope.builder()
                        .userId(userBudgetDTO.userId())
                        .name(name)
                        .description(description)
                        .status(SystemStatus.SYSTEM_STATUS_ACTIVE)
                        .build());

                BudgetEnvelope budgetEnvelope = budgetEnvelopeRepository.save(
                        BudgetEnvelope.builder()
                            .budgetId(userBudgetDTO.budgetId())
                            .envelopeId(envelope.getId())
                            .amount(amount)
                            .recurring(recurring)
                            .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                            .build()
                );

                BigDecimal totalExpenses = budgetEnvelopeRepository
                        .getTotalExpenses(userBudgetDTO.budgetId());

                budgetContextService.updateBudgetStatus(
                        userBudgetDTO.budgetId(),
                        totalExpenses
                );

                return EnvelopeResponseDTO.builder()
                        .envelopeId(envelope.getId())
                        .budgetEnvelopeId(budgetEnvelope.getId())
                        .build();
            };

            return transactionalHandler.runInTransactionSupplier(supplier);
        } catch (DataIntegrityViolationException e) {
            logger.error("DataIntegrityViolationException: {}", e.getMessage());

            throw new PeridotDuplicateException("Envelope already exists");
        } finally {
            logger.info("End - Service - createEnvelope");
        }

    }

    @Override
    public EnvelopeResponseDTO updateEnvelope(
            Integer budgetEnvelopeId,
            String name,
            String description,
            BigDecimal amount,
            Boolean recurring
    ) {
        logger.info("Start - Service - updateEnvelope");

        Supplier<EnvelopeResponseDTO> supplier = () -> {
            budgetEnvelopeRepository.updateEnvelope(
                    budgetEnvelopeId,
                    name,
                    description,
                    amount,
                    recurring
            );

            BudgetEnvelope budgetEnvelope = budgetEnvelopeRepository.getReferenceById(budgetEnvelopeId);

            BigDecimal totalExpenses = budgetEnvelopeRepository.getTotalExpenses(budgetEnvelope.getBudgetId());

            budgetContextService.updateBudgetStatus(budgetEnvelope.getBudgetId(), totalExpenses);

            return EnvelopeResponseDTO.builder()
                    .envelopeId(budgetEnvelope.getEnvelopeId())
                    .budgetEnvelopeId(budgetEnvelope.getId())
                    .build();
        };

        logger.info("End - Service - updateEnvelope");
        return transactionalHandler.runInTransactionSupplier(supplier);
    }
}
