package com.jonichi.peridot.envelope.controller;

import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.common.dto.SuccessResponse;
import com.jonichi.peridot.envelope.dto.CreateUpdateEnvelopeDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeDataDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.dto.PeridotPagination;
import com.jonichi.peridot.envelope.service.EnvelopeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * Updates an envelope's details.
     *
     * <p>This method processes a request to update the details of an envelope, including
     * its name, description, amount, and recurring status.</p>
     *
     * @param budgetEnvelopeId the unique identifier of the envelope to update.
     * @param createUpdateEnvelopeDTO the DTO containing the updated information for the envelope.
     * @return a {@link ResponseEntity} containing the updated envelope details in an API response.
     */
    @PutMapping("/{budgetEnvelopeId}")
    public ResponseEntity<ApiResponse<EnvelopeResponseDTO>> updateEnvelope(
            @PathVariable Integer budgetEnvelopeId,
            @RequestBody @Valid CreateUpdateEnvelopeDTO createUpdateEnvelopeDTO
    ) {
        logger.info("Start - Controller - updateEnvelope");
        logger.debug("""
                PathVariable: {}
                Request: {}
                """,
                budgetEnvelopeId,
                createUpdateEnvelopeDTO
        );

        EnvelopeResponseDTO envelopeResponseDTO = envelopeService.updateEnvelope(
                budgetEnvelopeId,
                createUpdateEnvelopeDTO.name(),
                createUpdateEnvelopeDTO.description(),
                createUpdateEnvelopeDTO.amount(),
                createUpdateEnvelopeDTO.recurring()
        );

        HttpStatus status = HttpStatus.OK;
        ApiResponse<EnvelopeResponseDTO> response = SuccessResponse.<EnvelopeResponseDTO>builder()
                .code(status.value())
                .message("Envelope updated successfully")
                .data(envelopeResponseDTO)
                .build();

        logger.info("End - Controller - updateEnvelope");
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Retrieves a paginated list of envelopes for a given budget.
     *
     * <p>This method fetches envelopes associated with the specified budget ID. It supports
     * pagination and sorting options.</p>
     *
     * @param budgetId the unique identifier of the budget whose envelopes are to be retrieved.
     * @param page the page number for pagination (optional).
     * @param size the number of items per page for pagination (optional).
     * @param sortBy the field to sort the envelopes by (optional).
     * @param sortDirection the direction of sorting, either 'asc' or 'desc' (optional).
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with paginated envelope
     *     data as {@link PeridotPagination}.
     */
    @GetMapping("/{budgetId}")
    public ResponseEntity<ApiResponse<PeridotPagination<EnvelopeDataDTO>>> getEnvelopes(
            @PathVariable Integer budgetId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    ) {
        logger.info("Start - Controller - getEnvelopes");
        logger.debug("""
                PathVariable: {}
                RequestParam: page={}, size={}, sortBy={}, sortDirection={}
                """,
                budgetId,
                page,
                size,
                sortBy,
                sortDirection
        );

        PeridotPagination<EnvelopeDataDTO> peridotPagination = envelopeService.getEnvelopes(
                budgetId,
                page,
                size,
                sortBy,
                sortDirection
        );

        HttpStatus status = HttpStatus.OK;
        ApiResponse<PeridotPagination<EnvelopeDataDTO>> response = SuccessResponse
                .<PeridotPagination<EnvelopeDataDTO>>builder()
                .code(status.value())
                .message("List of envelopes retrieved successfully")
                .data(peridotPagination)
                .build();

        logger.info("End - Controller - getEnvelopes");
        return ResponseEntity.status(status).body(response);
    }

}
