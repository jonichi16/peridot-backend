package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.AuthContextService;
import com.jonichi.peridot.budget.dto.BudgetDataDTO;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.model.BudgetStatus;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.budget.service.BudgetService;
import com.jonichi.peridot.common.exception.PeridotDuplicateException;
import com.jonichi.peridot.common.exception.PeridotNotFoundException;
import com.jonichi.peridot.common.util.DateUtil;
import com.jonichi.peridot.common.util.TransactionalHandler;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link BudgetService} that provides business logic for managing budgets.
 *
 * <p>This service handles the creation of budgets, ensuring that the necessary data
 * is validated and persisted correctly. It relies on {@link BudgetRepository} for persistence,
 * {@link AuthContextService} for fetching user details, and {@link TransactionalHandler} for
 * transactional operations.</p>
 */
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private static final Logger logger = LoggerFactory.getLogger(BudgetServiceImpl.class);
    private final BudgetRepository budgetRepository;
    private final AuthContextService authContextService;
    private final TransactionalHandler transactionalHandler;

    @Override
    public BudgetResponseDTO createBudget(BigDecimal amount) {
        logger.info("Start - Service - createBudget");

        try {
            Integer userId = authContextService.getUserId();

            Supplier<Budget> supplier = () -> budgetRepository.save(
                    Budget.builder()
                            .userId(userId)
                            .amount(amount)
                            .period(DateUtil.getCurrentPeriod())
                            .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                            .build()
            );

            Budget budget = transactionalHandler.runInTransactionSupplier(supplier);

            return BudgetResponseDTO.builder()
                    .budgetId(budget.getId())
                    .build();
        } catch (DataIntegrityViolationException e) {
            logger.error("DataIntegrityViolationException: {}", e.getMessage());

            throw new PeridotDuplicateException("Budget already exists");
        } finally {
            logger.info("End - Service - createBudget");
        }

    }

    @Override
    public BudgetDataDTO getCurrentBudget() {
        logger.info("Start - Service - getCurrentBudget");

        Integer userId = authContextService.getUserId();

        Budget budget = budgetRepository
                .getCurrentBudget(userId, DateUtil.getCurrentPeriod())
                .orElseThrow(() -> new PeridotNotFoundException("Budget does not exist"));

        logger.info("End - Service - getCurrentBudget");
        return BudgetDataDTO.builder()
                .period(budget.getPeriod())
                .amount(budget.getAmount())
                .status(budget.getStatus())
                .build();
    }

    @Override
    public BudgetResponseDTO updateCurrentBudget(BigDecimal amount) {
        logger.info("Start - Service - updateCurrentBudget");

        Integer userId = authContextService.getUserId();
        LocalDate currentPeriod = DateUtil.getCurrentPeriod();

        transactionalHandler.runInTransaction(() -> {
            budgetRepository.updateCurrentBudget(
                    userId,
                    currentPeriod,
                    amount
            );
        });

        Budget budget = budgetRepository
                .getCurrentBudget(userId, currentPeriod)
                .orElseThrow(() -> new PeridotNotFoundException("Budget does not exist"));

        logger.info("End - Service - updateCurrentBudget");
        return BudgetResponseDTO.builder()
                .budgetId(budget.getId())
                .build();
    }
}
