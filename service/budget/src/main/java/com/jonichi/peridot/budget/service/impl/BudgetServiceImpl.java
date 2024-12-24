package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.UserUtil;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.budget.service.BudgetService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserUtil userUtil;

    @Override
    public BudgetResponseDTO createBudget(BigDecimal amount) {
        Integer userId = userUtil.getUserId();
        Budget budget = Budget.builder()
                .userId(userId)
                .amount(amount)
                .build();

        budgetRepository.save(budget);

        return null;
    }
}
