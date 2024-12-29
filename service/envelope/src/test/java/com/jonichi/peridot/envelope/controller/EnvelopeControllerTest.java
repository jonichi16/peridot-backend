package com.jonichi.peridot.envelope.controller;

import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.envelope.dto.CreateUpdateEnvelopeDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeDataDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.dto.PeridotPagination;
import com.jonichi.peridot.envelope.model.BudgetEnvelopeStatus;
import com.jonichi.peridot.envelope.service.EnvelopeService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class EnvelopeControllerTest {

    @Mock
    private EnvelopeService envelopeService;
    @InjectMocks
    private EnvelopeController envelopeController;

    @Test
    public void createEnvelope_shouldReturnTheCorrectResponse() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("1000");
        CreateUpdateEnvelopeDTO createUpdateEnvelopeDTO = CreateUpdateEnvelopeDTO.builder()
                .name("Sample")
                .description("This is sample envelope")
                .amount(amount)
                .recurring(true)
                .build();
        EnvelopeResponseDTO envelopeResponseDTO = EnvelopeResponseDTO.builder()
                .envelopeId(1)
                .budgetEnvelopeId(1)
                .build();

        // when
        when(envelopeService.createEnvelope(
                createUpdateEnvelopeDTO.name(),
                createUpdateEnvelopeDTO.description(),
                createUpdateEnvelopeDTO.amount(),
                createUpdateEnvelopeDTO.recurring()
        )).thenReturn(envelopeResponseDTO);
        ResponseEntity<ApiResponse<EnvelopeResponseDTO>> response = envelopeController
                .createEnvelope(createUpdateEnvelopeDTO);

        // then
        verify(envelopeService, times(1)).createEnvelope(
                createUpdateEnvelopeDTO.name(),
                createUpdateEnvelopeDTO.description(),
                createUpdateEnvelopeDTO.amount(),
                createUpdateEnvelopeDTO.recurring()
        );
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(Objects.requireNonNull(response.getBody()).getData().envelopeId()).isEqualTo(1);
        assertThat(Objects.requireNonNull(response.getBody()).getData().budgetEnvelopeId()).isEqualTo(1);
        assertThat(response.getBody().getMessage()).isEqualTo("Envelope created successfully");
        assertThat(response.getBody().getCode()).isEqualTo(201);
    }

    @Test
    public void updateEnvelope_shouldReturnTheCorrectResponse() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("1000");
        CreateUpdateEnvelopeDTO createUpdateEnvelopeDTO = CreateUpdateEnvelopeDTO.builder()
                .name("Sample")
                .description("This is sample envelope")
                .amount(amount)
                .recurring(true)
                .build();
        EnvelopeResponseDTO envelopeResponseDTO = EnvelopeResponseDTO.builder()
                .envelopeId(1)
                .budgetEnvelopeId(1)
                .build();

        // when
        when(envelopeService.updateEnvelope(
                1,
                createUpdateEnvelopeDTO.name(),
                createUpdateEnvelopeDTO.description(),
                createUpdateEnvelopeDTO.amount(),
                createUpdateEnvelopeDTO.recurring()
        )).thenReturn(envelopeResponseDTO);
        ResponseEntity<ApiResponse<EnvelopeResponseDTO>> response = envelopeController
                .updateEnvelope(1, createUpdateEnvelopeDTO);

        // then
        verify(envelopeService, times(1)).updateEnvelope(
                1,
                createUpdateEnvelopeDTO.name(),
                createUpdateEnvelopeDTO.description(),
                createUpdateEnvelopeDTO.amount(),
                createUpdateEnvelopeDTO.recurring()
        );
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getData().envelopeId()).isEqualTo(1);
        assertThat(Objects.requireNonNull(response.getBody()).getData().budgetEnvelopeId()).isEqualTo(1);
        assertThat(response.getBody().getMessage()).isEqualTo("Envelope updated successfully");
        assertThat(response.getBody().getCode()).isEqualTo(200);
    }

    @Test
    public void getEnvelopes_shouldReturnPaginatedResponse() throws Exception {
        // given
        Integer budgetId = 1;
        Integer page = 1;
        Integer size = 10;
        String sortBy = "id";
        String sortDirection = "DESC";

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

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

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
        when(envelopeService.getEnvelopes(
                budgetId,
                page,
                size,
                sortBy,
                sortDirection
        )).thenReturn(peridotPagination);
        ResponseEntity<ApiResponse<PeridotPagination<EnvelopeDataDTO>>> response = envelopeController
                .getEnvelopes(
                        budgetId,
                        page,
                        size,
                        sortBy,
                        sortDirection
                );

        // then
        verify(envelopeService, times(1)).getEnvelopes(
                budgetId,
                page,
                size,
                sortBy,
                sortDirection);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).isEqualTo(peridotPagination);
        assertThat(response.getBody().getMessage()).isEqualTo("List of envelopes retrieved successfully");
        assertThat(response.getBody().getCode()).isEqualTo(200);
    }

}
