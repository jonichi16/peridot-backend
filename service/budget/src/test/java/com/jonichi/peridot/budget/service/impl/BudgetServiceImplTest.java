package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.UserUtil;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.model.BudgetStatus;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.common.util.TransactionalHandler;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Supplier;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    @Mock
    private TransactionalHandler transactionalHandler;
    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Test
    public void createBudget_shouldUseTransactionalHandler() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("1000");
        LocalDate period = LocalDate.of(2024, 12, 1);

        Budget budget = Budget.builder()
                .id(1)
                .userId(1)
                .amount(amount)
                .period(period)
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        // when
        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class))).thenReturn(budget);
        budgetService.createBudget(amount);

        // then
        verify(transactionalHandler, times(1)).runInTransactionSupplier(any(Supplier.class));
    }

    @Test
    public void createBudget_shouldReturnBudgetResponseDTO() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("1000");
        LocalDate period = LocalDate.of(2024, 12, 1);

        Budget budget = Budget.builder()
                .id(1)
                .userId(1)
                .amount(amount)
                .period(period)
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        // when
        when(userUtil.getUserId()).thenReturn(1);
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier<Budget> supplier = invocation.getArgument(0);
                    return supplier.get();
                });
        BudgetResponseDTO budgetResponseDTO = budgetService.createBudget(amount);

        // then
        verify(userUtil, times(1)).getUserId();
        verify(budgetRepository, times(1)).save(any(Budget.class));
        assertThat(budgetResponseDTO.budgetId()).isEqualTo(1);
    }

}
