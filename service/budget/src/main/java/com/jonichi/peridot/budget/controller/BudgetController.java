package com.jonichi.peridot.budget.controller;

import com.jonichi.peridot.budget.dto.BudgetDataDTO;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.dto.CreateUpdateBudgetDTO;
import com.jonichi.peridot.budget.service.BudgetService;
import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.common.dto.SuccessResponse;
import com.jonichi.peridot.common.util.DateUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
     * @param createUpdateBudgetDTO the request payload containing the budget amount
     * @return a response entity containing the created budget and a success message
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<BudgetResponseDTO>> createBudget(
            @RequestBody @Valid CreateUpdateBudgetDTO createUpdateBudgetDTO
    ) {
        logger.info("Start - Controller - createBudget");
        logger.debug(
                "Request: amount={}",
                createUpdateBudgetDTO.amount()
        );

        BudgetResponseDTO budgetResponseDTO = budgetService.createBudget(
                createUpdateBudgetDTO.amount()
        );

        HttpStatus status = HttpStatus.CREATED;
        ApiResponse<BudgetResponseDTO> response = SuccessResponse.<BudgetResponseDTO>builder()
                .code(status.value())
                .message("Budget created successfully")
                .data(budgetResponseDTO)
                .build();

        logger.info("End - Controller - createBudget");
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Retrieves the current budget for the authenticated user.
     *
     * <p>This method retrieves the budget data for the current period and returns it in a
     * {@link BudgetDataDTO}. The current period is determined based on the system date, and the
     * budget is fetched using the {@link BudgetService} service layer.</p>
     *
     * @return a {@link ResponseEntity} containing the status and the current budget data wrapped
     *     in an {@link ApiResponse} object.
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<BudgetDataDTO>> getCurrentBudget() {
        logger.info("Start - Controller - getCurrentBudget");
        logger.debug(
                "Current period = {}",
                DateUtil.getCurrentPeriod()
        );

        BudgetDataDTO budgetDataDTO = budgetService.getCurrentBudget();

        HttpStatus status = HttpStatus.OK;
        ApiResponse<BudgetDataDTO> response = SuccessResponse.<BudgetDataDTO>builder()
                .code(status.value())
                .message("Current budget retrieved successfully")
                .data(budgetDataDTO)
                .build();


        logger.info("End - Controller - getCurrentBudget");
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Updates the current budget with the provided details.
     *
     * <p>This method handles an HTTP PUT request to update the current budget's amount.
     * It validates the input, updates the budget through the service layer, and returns
     * a response indicating success.
     *
     * @param createUpdateBudgetDTO the request payload containing the budget amount
     * @return a response entity containing the updated budget and a success message
     */
    @PutMapping("/current")
    public ResponseEntity<ApiResponse<BudgetResponseDTO>> updateCurrentBudget(
            @RequestBody @Valid CreateUpdateBudgetDTO createUpdateBudgetDTO
    ) {
        logger.info("Start - Controller - updateCurrentBudget");
        logger.debug(
                "Request: new amount={}",
                createUpdateBudgetDTO.amount()
        );

        BudgetResponseDTO budgetResponseDTO = budgetService.updateCurrentBudget(
                createUpdateBudgetDTO.amount()
        );

        HttpStatus status = HttpStatus.OK;
        ApiResponse<BudgetResponseDTO> response = SuccessResponse.<BudgetResponseDTO>builder()
                .code(status.value())
                .message("Budget updated successfully")
                .data(budgetResponseDTO)
                .build();

        logger.info("End - Controller - updateCurrentBudget");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
