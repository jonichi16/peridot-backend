package com.jonichi.peridot.budget.controller;

import com.jonichi.peridot.budget.dto.BudgetDataDTO;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.dto.CreateUpdateBudgetDTO;
import com.jonichi.peridot.budget.model.BudgetStatus;
import com.jonichi.peridot.budget.service.BudgetService;
import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.common.util.DateUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;
    @InjectMocks
    private BudgetController budgetController;

    @Test
    public void createBudget_shouldCallBudgetServiceOnce() throws Exception {
        // given
        CreateUpdateBudgetDTO createUpdateBudgetDTO = CreateUpdateBudgetDTO.builder()
                .amount(new BigDecimal("1000"))
                .build();

        // when
        budgetController.createBudget(createUpdateBudgetDTO);

        // then
        verify(budgetService, times(1)).createBudget(createUpdateBudgetDTO.amount());
    }
    
    @Test
    public void createBudget_shouldReturnCorrectResponse() throws Exception {
        // given
        CreateUpdateBudgetDTO createUpdateBudgetDTO = CreateUpdateBudgetDTO.builder()
                .amount(new BigDecimal("1000"))
                .build();
        BudgetResponseDTO budgetResponseDTO = BudgetResponseDTO.builder()
                .budgetId(1)
                .build();
        
        // when
        when(budgetService.createBudget(createUpdateBudgetDTO.amount())).thenReturn(budgetResponseDTO);
        ResponseEntity<ApiResponse<BudgetResponseDTO>> response = budgetController.createBudget(createUpdateBudgetDTO);
        
        // then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(Objects.requireNonNull(response.getBody()).getData().budgetId()).isEqualTo(1);
        assertThat(response.getBody().getMessage()).isEqualTo("Budget created successfully");
        assertThat(response.getBody().getCode()).isEqualTo(201);
    }

    @Test
    public void getCurrentBudget_shouldReturnTheCurrentBudget() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("1000");
        LocalDate currentPeriod = DateUtil.getCurrentPeriod();
        BudgetDataDTO budgetDataDTO = BudgetDataDTO.builder()
                .period(currentPeriod)
                .amount(amount)
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        // when
        when(budgetService.getCurrentBudget()).thenReturn(budgetDataDTO);
        ResponseEntity<ApiResponse<BudgetDataDTO>> response = budgetController.getCurrentBudget();

        // then
        verify(budgetService, times(1)).getCurrentBudget();
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).isSuccess()).isTrue();
        assertThat(response.getBody().getData()).isEqualTo(budgetDataDTO);
        assertThat(response.getBody().getCode()).isEqualTo(200);
        assertThat(response.getBody().getMessage()).isEqualTo("Current budget retrieved successfully");
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    public void updateCurrentBudget_shouldReturnCorrectResponse() throws Exception {
        // given

        // when

        // then
    }
}
