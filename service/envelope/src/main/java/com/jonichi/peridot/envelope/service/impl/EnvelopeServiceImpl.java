package com.jonichi.peridot.envelope.service.impl;

import com.jonichi.peridot.budget.service.BudgetContextService;
import com.jonichi.peridot.common.dto.UserBudgetDTO;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvelopeServiceImpl implements EnvelopeService {

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
        UserBudgetDTO userBudgetDTO = budgetContextService.getCurrentUserBudgetId();

        Supplier<EnvelopeResponseDTO> supplier = () -> {
            Envelope envelope = envelopeRepository.save(Envelope.builder()
                    .userId(userBudgetDTO.userId())
                    .name(name)
                    .description(description)
                    .status(SystemStatus.SYSTEM_STATUS_ACTIVE)
                    .build());

            BudgetEnvelope budgetEnvelope = budgetEnvelopeRepository.save(BudgetEnvelope.builder()
                    .budgetId(userBudgetDTO.budgetId())
                    .envelopeId(envelope.getId())
                    .amount(amount)
                    .recurring(recurring)
                    .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                    .build());

            return EnvelopeResponseDTO.builder()
                    .envelopeId(envelope.getId())
                    .budgetEnvelopeId(budgetEnvelope.getId())
                    .build();
        };

        return transactionalHandler.runInTransactionSupplier(supplier);
    }
}
