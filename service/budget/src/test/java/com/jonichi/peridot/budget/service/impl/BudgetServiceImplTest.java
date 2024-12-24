package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.UserUtil;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

        // when
        when(userUtil.getUserId()).thenReturn(1);
        budgetService.createBudget(amount);

        // then
        verify(userUtil, times(1)).getUserId();
    }

}
