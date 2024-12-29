package com.jonichi.peridot.envelope.controller;

import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.envelope.dto.CreateUpdateEnvelopeDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.service.EnvelopeService;
import java.math.BigDecimal;
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

}
