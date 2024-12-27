package com.jonichi.peridot.envelope.service.impl;

import com.jonichi.peridot.budget.model.Budget;
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
import java.math.BigDecimal;
import java.util.function.Supplier;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EnvelopeServiceImplTest {

    @Mock
    private EnvelopeRepository envelopeRepository;
    @Mock
    private BudgetEnvelopeRepository budgetEnvelopeRepository;
    @Mock
    private BudgetContextService budgetContextService;
    @Mock
    private TransactionalHandler transactionalHandler;
    @InjectMocks
    private EnvelopeServiceImpl envelopeServiceImpl;

    @Test
    public void createEnvelope_should() throws Exception {
        // given
        String name = "Sample";
        String description = "This is sample envelope";
        BigDecimal amount = new BigDecimal("1000");
        EnvelopeResponseDTO envelopeResponseDTO = EnvelopeResponseDTO.builder()
                .envelopeId(1)
                .budgetEnvelopeId(1)
                .build();
        Envelope envelope = Envelope.builder()
                .id(1)
                .build();
        BudgetEnvelope budgetEnvelope = BudgetEnvelope.builder()
                .id(1)
                .build();

        Envelope envelopeRequest = Envelope.builder()
                .userId(1)
                .name(name)
                .description(description)
                .status(SystemStatus.SYSTEM_STATUS_ACTIVE)
                .build();
        BudgetEnvelope budgetEnvelopeRequest = BudgetEnvelope.builder()
                .budgetId(1)
                .envelopeId(1)
                .amount(amount)
                .recurring(true)
                .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                .build();

        // when
        when(budgetContextService.getCurrentUserBudgetId()).thenReturn(
                UserBudgetDTO.builder()
                        .budgetId(1)
                        .userId(1)
                        .build()
        );
        when(envelopeRepository.save(envelopeRequest)).thenReturn(envelope);
        when(budgetEnvelopeRepository.save(budgetEnvelopeRequest)).thenReturn(budgetEnvelope);
        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier<Budget> supplier = invocation.getArgument(0);
                    return supplier.get();
                });
        EnvelopeResponseDTO response = envelopeServiceImpl.createEnvelope(
                name,
                description,
                amount,
                true
        );

        // then
        verify(budgetContextService, times(1)).getCurrentUserBudgetId();
        verify(transactionalHandler, times(1)).runInTransactionSupplier(any(Supplier.class));
        verify(envelopeRepository, times(1)).save(envelopeRequest);
        verify(budgetEnvelopeRepository, times(1)).save(budgetEnvelopeRequest);
        assertThat(response.envelopeId()).isEqualTo(1);
        assertThat(response.budgetEnvelopeId()).isEqualTo(1);
    }
}
