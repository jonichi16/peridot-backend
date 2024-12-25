package com.jonichi.peridot.budget.service.impl;

import com.jonichi.peridot.auth.service.UserUtil;
import com.jonichi.peridot.budget.dto.BudgetDataDTO;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.model.Budget;
import com.jonichi.peridot.budget.model.BudgetStatus;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.common.exception.PeridotDuplicateException;
import com.jonichi.peridot.common.exception.PeridotNotFoundException;
import com.jonichi.peridot.common.util.DateUtil;
import com.jonichi.peridot.common.util.TransactionalHandler;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
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

    @Test
    public void createBudget_withDuplicateUserAndPeriod_shouldThrowPeridotDuplicateError() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("1000");

        // when
        when(userUtil.getUserId()).thenReturn(1);
        when(budgetRepository.save(any(Budget.class)))
                .thenThrow(
                        new DataIntegrityViolationException(
                                "Detail: Key (user_id, period)=(1, 2024-12-01) already exists."
                        )
                );
        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier<Budget> supplier = invocation.getArgument(0);
                    return supplier.get();
                });

        // then
        assertThatThrownBy(() -> budgetService.createBudget(amount))
                .isInstanceOf(PeridotDuplicateException.class)
                .hasMessage("Budget already exists");
    }

    @Test
    public void getCurrentBudget_shouldReturnBudgetDataDTO() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("1000");
        LocalDate currentPeriod = DateUtil.getCurrentPeriod();
        Budget budget = Budget.builder()
                .id(1)
                .userId(1)
                .amount(amount)
                .period(currentPeriod)
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        // when
        when(userUtil.getUserId()).thenReturn(1);
        when(budgetRepository.getCurrentBudget(1, currentPeriod)).thenReturn(Optional.of(budget));
        BudgetDataDTO response = budgetService.getCurrentBudget();

        // then
        verify(budgetRepository, times(1)).getCurrentBudget(1, currentPeriod);
        assertThat(response).isEqualTo(response);

    }

    @Test
    public void getCurrentBudget_withNoBudget_shouldThrowPeridotNotFoundException() throws Exception {
        // given
        LocalDate currentPeriod = DateUtil.getCurrentPeriod();

        // when
        when(userUtil.getUserId()).thenReturn(1);
        when(budgetRepository.getCurrentBudget(1, currentPeriod)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> budgetService.getCurrentBudget())
                .isInstanceOf(PeridotNotFoundException.class)
                .hasMessage("Budget does not exist");
    }

}
