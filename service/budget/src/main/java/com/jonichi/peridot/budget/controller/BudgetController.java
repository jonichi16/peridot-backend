package com.jonichi.peridot.budget.controller;

import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.dto.CreateBudgetDTO;
import com.jonichi.peridot.budget.service.BudgetService;
import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.common.dto.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing budgets.
 *
 * <p>Provides endpoints for creating and managing budget resources.
 * This controller interacts with the {@code BudgetService} to perform the business logic
 * and returns appropriate HTTP responses.</p>
 */
@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);
    private final BudgetService budgetService;

    /**
     * Creates a new budget.
     *
     * <p>This endpoint receives a {@code CreateBudgetDTO} payload with the budget details,
     * invokes the {@code BudgetService} to process the request, and returns the created budget
     * information.</p>
     *
     * @param createBudgetDTO the request payload containing the budget amount
     * @return a response entity containing the created budget and a success message
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<BudgetResponseDTO>> createBudget(
            @RequestBody @Valid CreateBudgetDTO createBudgetDTO
    ) {
        logger.info("Start - Controller - createBudget");
        logger.debug(
                "Request: amount={}",
                createBudgetDTO.amount()
        );

        BudgetResponseDTO budgetResponseDTO = budgetService.createBudget(createBudgetDTO.amount());

        HttpStatus status = HttpStatus.CREATED;
        ApiResponse<BudgetResponseDTO> response = SuccessResponse.<BudgetResponseDTO>builder()
                .code(status.value())
                .message("Budget created successfully")
                .data(budgetResponseDTO)
                .build();

        logger.info("End - Controller - createBudget");
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getBudgetById() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
