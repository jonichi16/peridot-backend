package com.jonichi.peridot.budget.service;

import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public interface BudgetService {
    BudgetResponseDTO createBudget(BigDecimal amount);
}
