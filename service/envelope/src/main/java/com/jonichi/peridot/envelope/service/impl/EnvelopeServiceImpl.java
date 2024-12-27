package com.jonichi.peridot.envelope.service.impl;

import com.jonichi.peridot.common.util.TransactionalHandler;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.service.EnvelopeService;
import java.math.BigDecimal;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvelopeServiceImpl implements EnvelopeService {

    private final TransactionalHandler transactionalHandler;

    @Override
    public EnvelopeResponseDTO createEnvelope(
            String name,
            String description,
            BigDecimal amount,
            Boolean recurring
    ) {

        Supplier<EnvelopeResponseDTO> supplier = () -> EnvelopeResponseDTO.builder().build();

        EnvelopeResponseDTO envelopeResponseDTO = transactionalHandler.runInTransactionSupplier(supplier);

        return null;
    }
}
