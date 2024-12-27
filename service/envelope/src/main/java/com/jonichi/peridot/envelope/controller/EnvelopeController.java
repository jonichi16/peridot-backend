package com.jonichi.peridot.envelope.controller;

import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.common.dto.SuccessResponse;
import com.jonichi.peridot.envelope.dto.CreateEnvelopeDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.service.EnvelopeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/envelopes")
@RequiredArgsConstructor
public class EnvelopeController {

    private static final Logger logger = LoggerFactory.getLogger(EnvelopeController.class);
    private final EnvelopeService envelopeService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<EnvelopeResponseDTO>> createEnvelope(
            @RequestBody @Valid CreateEnvelopeDTO createEnvelopeDTO
    ) {
        logger.info("Start - Controller - createBudget");
        logger.debug("Request: {}", createEnvelopeDTO);

        EnvelopeResponseDTO envelopeResponseDTO = envelopeService.createEnvelope(
                createEnvelopeDTO.name(),
                createEnvelopeDTO.description(),
                createEnvelopeDTO.amount(),
                createEnvelopeDTO.recurring()
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

}
