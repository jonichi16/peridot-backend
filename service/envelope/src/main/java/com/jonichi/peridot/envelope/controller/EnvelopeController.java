package com.jonichi.peridot.envelope.controller;

import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.common.dto.SuccessResponse;
import com.jonichi.peridot.envelope.dto.CreateUpdateEnvelopeDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.service.EnvelopeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing envelopes.
 *
 * <p>This controller handles API endpoints related to envelope management, including creating
 * envelopes. It interacts with the service layer to perform business operations.</p>
 */
@RestController
@RequestMapping("/api/envelopes")
@RequiredArgsConstructor
public class EnvelopeController {

    private static final Logger logger = LoggerFactory.getLogger(EnvelopeController.class);
    private final EnvelopeService envelopeService;

    /**
     * Creates a new envelope.
     *
     * <p>This endpoint handles HTTP POST requests to create a new envelope. The request body must
     * contain a valid {@link CreateUpdateEnvelopeDTO}. The created envelope's details are returned
     * in the response.</p>
     *
     * @param createUpdateEnvelopeDTO the DTO containing details for the new envelope.
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with the created envelope
     *         details in {@link EnvelopeResponseDTO} and an HTTP status of 201 (Created).
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<EnvelopeResponseDTO>> createEnvelope(
            @RequestBody @Valid CreateUpdateEnvelopeDTO createUpdateEnvelopeDTO
    ) {
        logger.info("Start - Controller - createBudget");
        logger.debug("Request: {}", createUpdateEnvelopeDTO);

        EnvelopeResponseDTO envelopeResponseDTO = envelopeService.createEnvelope(
                createUpdateEnvelopeDTO.name(),
                createUpdateEnvelopeDTO.description(),
                createUpdateEnvelopeDTO.amount(),
                createUpdateEnvelopeDTO.recurring()
        );

        HttpStatus status = HttpStatus.CREATED;
        ApiResponse<EnvelopeResponseDTO> response = SuccessResponse.<EnvelopeResponseDTO>builder()
                .code(status.value())
                .message("Envelope created successfully")
                .data(envelopeResponseDTO)
                .build();

        logger.info("End - Controller - createBudget");
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/{budgetEnvelopeId}")
    public ResponseEntity<?> updateEnvelope(
            @RequestBody @Valid CreateUpdateEnvelopeDTO createUpdateEnvelopeDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
