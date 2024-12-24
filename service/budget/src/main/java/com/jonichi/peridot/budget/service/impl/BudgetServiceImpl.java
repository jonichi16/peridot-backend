package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.UserUtil;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.model.BudgetStatus;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.budget.service.BudgetService;
import com.jonichi.peridot.common.util.DateUtil;
import com.jonichi.peridot.common.util.TransactionalHandler;
import java.math.BigDecimal;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link BudgetService} that provides business logic for managing budgets.
 *
 * <p>This service handles the creation of budgets, ensuring that the necessary data
 * is validated and persisted correctly. It relies on {@link BudgetRepository} for persistence,
 * {@link UserUtil} for fetching user details, and {@link TransactionalHandler} for transactional
 * operations.</p>
 */
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private static final Logger logger = LoggerFactory.getLogger(BudgetServiceImpl.class);
    private final BudgetRepository budgetRepository;
    private final UserUtil userUtil;
    private final TransactionalHandler transactionalHandler;

    @Override
    public BudgetResponseDTO createBudget(BigDecimal amount) {
        logger.info("Start - Service - createBudget");

        Integer userId = userUtil.getUserId();

        Supplier<Budget> supplier = () -> budgetRepository.save(
                Budget.builder()
                        .userId(userId)
                        .amount(amount)
                        .period(DateUtil.getCurrentPeriod())
                        .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                        .build()
        );

        Budget budget = transactionalHandler.runInTransactionSupplier(supplier);

        logger.info("End - Service - createBudget");
        return BudgetResponseDTO.builder()
                .budgetId(budget.getId())
                .build();
    }
}
