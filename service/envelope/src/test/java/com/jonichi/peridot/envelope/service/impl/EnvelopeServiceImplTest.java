package com.jonichi.peridot.envelope.service.impl;

import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.service.BudgetContextService;
import com.jonichi.peridot.common.dto.UserBudgetDTO;
import com.jonichi.peridot.common.exception.PeridotDuplicateException;
import com.jonichi.peridot.common.exception.PeridotNotFoundException;
import com.jonichi.peridot.common.model.SystemStatus;
import com.jonichi.peridot.common.util.TransactionalHandler;
import com.jonichi.peridot.envelope.dto.EnvelopeDataDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.dto.PeridotPagination;
import com.jonichi.peridot.envelope.model.BudgetEnvelope;
import com.jonichi.peridot.envelope.model.BudgetEnvelopeStatus;
import com.jonichi.peridot.envelope.model.Envelope;
import com.jonichi.peridot.envelope.repository.BudgetEnvelopeRepository;
import com.jonichi.peridot.envelope.repository.EnvelopeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    public void createEnvelope_shouldCReturnEnvelope() throws Exception {
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
        when(budgetEnvelopeRepository.getTotalExpenses(1)).thenReturn(BigDecimal.valueOf(1000));
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
        verify(budgetContextService, times(1)).updateBudgetStatus(1, BigDecimal.valueOf(1000));
        assertThat(response.envelopeId()).isEqualTo(1);
        assertThat(response.budgetEnvelopeId()).isEqualTo(1);
    }

    @Test
    public void createEnvelope_withDuplicate_shouldThrowPeridotDuplicateException() throws Exception {
        // given
        String name = "Sample";
        String description = "This is sample envelope";
        BigDecimal amount = new BigDecimal("1000");

        // when
        when(budgetContextService.getCurrentUserBudgetId()).thenReturn(
                UserBudgetDTO.builder()
                        .budgetId(1)
                        .userId(1)
                        .build()
        );
        when(envelopeRepository.save(any(Envelope.class)))
                .thenThrow(
                        new DataIntegrityViolationException(
                                "Detail: Key (user_id, name)=(1, Sample) already exists."
                        )
                );
        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier<EnvelopeResponseDTO> supplier = invocation.getArgument(0);
                    return supplier.get();
                });

        // then
        assertThatThrownBy(() -> envelopeServiceImpl.createEnvelope(
                name,
                description,
                amount,
                true
        )).isInstanceOf(PeridotDuplicateException.class)
                .hasMessage("Envelope already exists");
    }

    @Test
    public void updateEnvelope_shouldUpdateEnvelopeAndReturnEnvelope() throws Exception {
        // given
        Integer budgetEnvelopeId = 1;
        String name = "Sample";
        String description = "This is sample";
        BigDecimal amount = BigDecimal.valueOf(1000);
        BudgetEnvelope budgetEnvelope = BudgetEnvelope.builder()
                .id(budgetEnvelopeId)
                .budgetId(1)
                .envelopeId(1)
                .amount(amount)
                .recurring(true)
                .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                .build();

        // when
        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier<Budget> supplier = invocation.getArgument(0);
                    return supplier.get();
                });
        when(budgetEnvelopeRepository.getReferenceById(budgetEnvelopeId)).thenReturn(budgetEnvelope);
        when(budgetEnvelopeRepository.getTotalExpenses(budgetEnvelope.getBudgetId())).thenReturn(BigDecimal.valueOf(2000));
        EnvelopeResponseDTO response = envelopeServiceImpl.updateEnvelope(
                budgetEnvelopeId,
                name,
                description,
                amount,
                true
        );

        // then
        verify(transactionalHandler, times(1)).runInTransactionSupplier(any(Supplier.class));
        verify(budgetEnvelopeRepository, times(1)).getReferenceById(budgetEnvelopeId);
        verify(envelopeRepository, times(1)).updateEnvelope(
                1,
                name,
                description
        );
        verify(budgetEnvelopeRepository, times(1)).updateBudgetEnvelope(
                budgetEnvelopeId,
                amount,
                true
        );
        verify(budgetEnvelopeRepository, times(1)).getTotalExpenses(budgetEnvelope.getBudgetId());
        verify(budgetContextService, times(1)).updateBudgetStatus(budgetEnvelope.getBudgetId(), BigDecimal.valueOf(2000));
        assertThat(response.envelopeId()).isEqualTo(1);
        assertThat(response.budgetEnvelopeId()).isEqualTo(1);

    }

    @Test
    public void updateEnvelope_withDuplicateEnvelope_shouldThrowPeridotDuplicateException() throws Exception {
        // given
        Integer budgetEnvelopeId = 1;
        String name = "Sample";
        String description = "This is sample";
        BigDecimal amount = BigDecimal.valueOf(1000);
        BudgetEnvelope budgetEnvelope = BudgetEnvelope.builder()
                .id(budgetEnvelopeId)
                .budgetId(1)
                .envelopeId(1)
                .amount(amount)
                .recurring(true)
                .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                .build();

        // when
        when(budgetEnvelopeRepository.getReferenceById(budgetEnvelopeId)).thenReturn(budgetEnvelope);
        when(envelopeRepository.updateEnvelope(
                1,
                name,
                description
        )).thenThrow(
                new DataIntegrityViolationException(
                        "Detail: Key (user_id, name)=(1, Sample) already exists."
                ));
        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier<EnvelopeResponseDTO> supplier = invocation.getArgument(0);
                    return supplier.get();
                });

        // then
        assertThatThrownBy(() -> envelopeServiceImpl.updateEnvelope(
                budgetEnvelopeId,
                name,
                description,
                amount,
                true
        )).isInstanceOf(PeridotDuplicateException.class)
                .hasMessage("Envelope already exists");
    }

    @Test
    public void updateEnvelope_withEnvelopeNotExist_shouldThrowPeridotNotFoundException() throws Exception {
        // given
        Integer budgetEnvelopeId = 1;
        String name = "Sample";
        String description = "This is sample";
        BigDecimal amount = BigDecimal.valueOf(1000);

        // when
        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier<EnvelopeResponseDTO> supplier = invocation.getArgument(0);
                    return supplier.get();
                });

        // then
        assertThatThrownBy(() -> envelopeServiceImpl.updateEnvelope(
                budgetEnvelopeId,
                name,
                description,
                amount,
                true
        )).isInstanceOf(PeridotNotFoundException.class)
                .hasMessage("Envelope does not exist");
    }

    @Test
    public void getEnvelopes_shouldReturnPeridotPaginationOfEnvelopeDataDTO() throws Exception {
        // given
        Integer budgetId = 1;
        Integer page = 1;
        Integer size = 10;
        String sortBy = "id";
        String sortDirection = "asc";

        List<EnvelopeDataDTO> envelopes = List.of(
                EnvelopeDataDTO.builder()
                        .name("Sample 1")
                        .description("This is sample envelope")
                        .amount(BigDecimal.valueOf(1000))
                        .recurring(true)
                        .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                        .build(),
                EnvelopeDataDTO.builder()
                        .name("Sample 2")
                        .description("This is sample envelope")
                        .amount(BigDecimal.valueOf(500))
                        .recurring(true)
                        .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                        .build()
        );

        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        Page<EnvelopeDataDTO> pagination = new PageImpl<>(envelopes, pageable, envelopes.size());

        PeridotPagination<EnvelopeDataDTO> peridotPagination = PeridotPagination.<EnvelopeDataDTO>builder()
                .content(envelopes)
                .totalPages(pagination.getTotalPages())
                .totalElements(pagination.getTotalElements())
                .numberOfElements(pagination.getNumberOfElements())
                .currentPage(pagination.getNumber() + 1)
                .first(pagination.isFirst())
                .last(pagination.isLast())
                .build();

        // when
        when(budgetEnvelopeRepository.getEnvelopes(budgetId, pageable)).thenReturn(pagination);
        PeridotPagination<EnvelopeDataDTO> response = envelopeServiceImpl.getEnvelopes(
                budgetId,
                page,
                size,
                sortBy,
                sortDirection
        );

        // then
        verify(budgetEnvelopeRepository, times(1)).getEnvelopes(budgetId, pageable);
        assertThat(response).isEqualTo(peridotPagination);
    }
}
