package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.AuthContextService;
import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.model.BudgetStatus;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.common.dto.UserBudgetDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class BudgetContextServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private AuthContextService authContextService;
    @InjectMocks
    private BudgetContextServiceImpl budgetContextService;

    @Test
    public void getCurrentBudgetId_shouldReturnCurrentUserBudgetId() throws Exception {
        // given
        Integer userId = 1;
        LocalDate period = LocalDate.of(2024, 12, 1);
        Budget budget = Budget.builder()
                .id(1)
                .userId(userId)
                .amount(new BigDecimal("1000"))
                .period(period)
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        // when
        when(authContextService.getUserId()).thenReturn(1);
        when(budgetRepository.getCurrentBudget(userId, period)).thenReturn(Optional.of(budget));
        UserBudgetDTO userBudgetDTO = budgetContextService.getCurrentUserBudgetId();

        // then
        verify(authContextService, times(1)).getUserId();
        verify(budgetRepository, times(1)).getCurrentBudget(userId, period);
        assertThat(userBudgetDTO.userId()).isEqualTo(1);
        assertThat(userBudgetDTO.budgetId()).isEqualTo(1);
    }

    @Test
    public void updateBudgetStatus_withBudgetAllocated_shouldUpdateStatusToComplete() throws Exception {
        // given
        Integer budgetId = 1;
        BigDecimal totalExpenses = new BigDecimal("1500");
        BudgetStatus expectedStatus = BudgetStatus.BUDGET_STATUS_COMPLETE;
        Budget budget = Budget.builder()
                .id(1)
                .userId(1)
                .amount(new BigDecimal("1500"))
                .period(LocalDate.of(2024, 12, 1))
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        // when
        when(budgetRepository.getReferenceById(budgetId)).thenReturn(budget);
        budgetContextService.updateBudgetStatus(budgetId, totalExpenses);

        // then
        verify(budgetRepository, times(1)).updateBudgetStatus(budgetId, expectedStatus);
    }

    @Test
    public void updateBudgetStatus_withBudgetNotAllocated_shouldUpdateStatusToIncomplete() throws Exception {
        // given
        Integer budgetId = 1;
        BigDecimal totalExpenses = new BigDecimal("1500");
        BudgetStatus expectedStatus = BudgetStatus.BUDGET_STATUS_INCOMPLETE;
        Budget budget = Budget.builder()
                .id(1)
                .userId(1)
                .amount(new BigDecimal("2000"))
                .period(LocalDate.of(2024, 12, 1))
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        // when
        when(budgetRepository.getReferenceById(budgetId)).thenReturn(budget);
        budgetContextService.updateBudgetStatus(budgetId, totalExpenses);

        // then
        verify(budgetRepository, times(1)).updateBudgetStatus(budgetId, expectedStatus);
    }

    @Test
    public void updateBudgetStatus_withTotalExpensesExceedBudget_shouldUpdateStatusToInvalid() throws Exception {
        // given
        Integer budgetId = 1;
        BigDecimal totalExpenses = new BigDecimal("5000");
        BudgetStatus expectedStatus = BudgetStatus.BUDGET_STATUS_INVALID;
        Budget budget = Budget.builder()
                .id(1)
                .userId(1)
                .amount(new BigDecimal("2000"))
                .period(LocalDate.of(2024, 12, 1))
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        // when
        when(budgetRepository.getReferenceById(budgetId)).thenReturn(budget);
        budgetContextService.updateBudgetStatus(budgetId, totalExpenses);

        // then
        verify(budgetRepository, times(1)).updateBudgetStatus(budgetId, expectedStatus);
    }

}
