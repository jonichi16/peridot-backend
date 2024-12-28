package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.AuthContextService;
import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.model.BudgetStatus;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.budget.service.BudgetContextService;
import com.jonichi.peridot.common.dto.UserBudgetDTO;
import com.jonichi.peridot.common.exception.PeridotNotFoundException;
import com.jonichi.peridot.common.util.DateUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link BudgetContextService} interface for managing user budget context.
 *
 * <p>This service handles retrieving the current user's budget ID. It interacts with the
 * {@link BudgetRepository} to fetch the current budget based on the authenticated user's ID
 * and the current period. If no budget is found, a {@link PeridotNotFoundException} is thrown.</p>
 */
@Service
@RequiredArgsConstructor
public class BudgetContextServiceImpl implements BudgetContextService {

    private static final Logger logger = LoggerFactory.getLogger(BudgetContextServiceImpl.class);
    private final BudgetRepository budgetRepository;
    private final AuthContextService authContextService;

    @Override
    public UserBudgetDTO getCurrentUserBudgetId() {
        logger.info("Start - Service - getCurrentUserBudgetId");

        Integer userId = authContextService.getUserId();
        LocalDate currentPeriod = DateUtil.getCurrentPeriod();
        Budget budget = budgetRepository.getCurrentBudget(userId, currentPeriod)
                .orElseThrow(() -> new PeridotNotFoundException("Budget does not exist"));

        logger.info("End - Service - getCurrentUserBudgetId");
        return UserBudgetDTO.builder()
                .userId(userId)
                .budgetId(budget.getId())
                .build();
    }

    @Override
    public void updateBudgetStatus(Integer budgetId, BigDecimal totalExpenses) {
        logger.info("Start - Service - updateBudgetStatus");

        BudgetStatus updatedStatus = BudgetStatus.BUDGET_STATUS_INCOMPLETE;
        BigDecimal budgetAmount = budgetRepository.getReferenceById(budgetId).getAmount();

        if (budgetAmount.compareTo(totalExpenses) == 0) {
            updatedStatus = BudgetStatus.BUDGET_STATUS_COMPLETE;
        } else if (budgetAmount.compareTo(totalExpenses) < 0) {
            updatedStatus = BudgetStatus.BUDGET_STATUS_INVALID;
        }

        budgetRepository.updateBudgetStatus(budgetId, updatedStatus);
        logger.info("End - Service - updateBudgetStatus");
    }
}
