package com.jonichi.peridot.envelope.service.impl;

import com.jonichi.peridot.common.util.TransactionalHandler;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.repository.BudgetEnvelopeRepository;
import com.jonichi.peridot.envelope.repository.EnvelopeRepository;
import java.math.BigDecimal;
import java.util.function.Supplier;
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

        // when
        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class))).thenReturn(envelopeResponseDTO);
        envelopeServiceImpl.createEnvelope(
                name,
                description,
                amount,
                true
        );

        // then
        verify(transactionalHandler, times(1)).runInTransactionSupplier(any(Supplier.class));
    }
}
