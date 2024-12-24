package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.UserUtil;
import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private UserUtil userUtil;
    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Test
    public void createBudget_shouldCallUserUtilAndBudgetRepositoryOnce() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("1000");
        Budget budget = Budget.builder()
                .userId(1)
                .amount(amount)
                .build();

        // when
        when(userUtil.getUserId()).thenReturn(1);
        when(budgetRepository.save(budget)).thenReturn(budget);
        budgetService.createBudget(amount);

        // then
        verify(userUtil, times(1)).getUserId();
        verify(budgetRepository, times(1)).save(budget);
    }

}
