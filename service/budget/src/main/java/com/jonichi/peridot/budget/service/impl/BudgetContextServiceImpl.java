package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.AuthContextService;
import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.budget.service.BudgetContextService;
import com.jonichi.peridot.common.dto.UserBudgetDTO;
import com.jonichi.peridot.common.exception.PeridotNotFoundException;
import com.jonichi.peridot.common.util.DateUtil;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
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

    private final BudgetRepository budgetRepository;
    private final AuthContextService authContextService;

    @Override
    public UserBudgetDTO getCurrentUserBudgetId() {
        Integer userId = authContextService.getUserId();
        LocalDate currentPeriod = DateUtil.getCurrentPeriod();
        Budget budget = budgetRepository.getCurrentBudget(userId, currentPeriod)
                .orElseThrow(() -> new PeridotNotFoundException("Budget does not exist"));

        return UserBudgetDTO.builder()
                .userId(userId)
                .budgetId(budget.getId())
                .build();
    }
}
