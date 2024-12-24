package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.UserUtil;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.service.BudgetService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final UserUtil userUtil;

    @Override
    public BudgetResponseDTO createBudget(BigDecimal amount) {
        Integer userId = userUtil.getUserId();

        return null;
    }
}
