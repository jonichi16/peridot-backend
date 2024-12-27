package com.jonichi.peridot.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonichi.peridot.auth.config.SecurityConfig;
import com.jonichi.peridot.auth.repository.UserRepository;
import com.jonichi.peridot.budget.dto.BudgetDataDTO;
import com.jonichi.peridot.budget.dto.BudgetResponseDTO;
import com.jonichi.peridot.budget.dto.CreateUpdateBudgetDTO;
import com.jonichi.peridot.budget.model.BudgetStatus;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.budget.service.BudgetService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class BudgetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BudgetRepository budgetRepository;
    @MockBean
    private BudgetService budgetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void createBudget_shouldReturn201Created() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("5000.00");
        CreateUpdateBudgetDTO createUpdateBudgetDTO = CreateUpdateBudgetDTO.builder()
                .amount(amount)
                .build();
        BudgetResponseDTO budgetResponseDTO = BudgetResponseDTO.builder()
                .budgetId(1)
                .build();

        // when
        when(budgetService.createBudget(amount)).thenReturn(budgetResponseDTO);

        // then
        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateBudgetDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(
                        document("createBudgetSuccess",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @WithMockUser
    public void createBudget_withMissingFields_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ }";

        // when

        // then
        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("createBudgetMissingFields",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @WithMockUser
    public void createBudget_withLessThanZeroAmount_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ \"amount\": \"0.0\" }";

        // when

        // then
        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("createBudgetInvalidRequest",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @WithMockUser
    public void createBudget_withTooLargeDigit_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ \"amount\": \"12345678901.00\" }";

        // when

        // then
        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBudget_withNoUser_shouldReturn403Forbidden() throws Exception {
        // given
        CreateUpdateBudgetDTO createUpdateBudgetDTO = new CreateUpdateBudgetDTO(new BigDecimal("1000"));

        // when

        // then
        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateBudgetDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void getCurrentBudget_shouldReturn200Ok() throws Exception {

        BudgetDataDTO budgetDataDTO = BudgetDataDTO.builder()
                .amount(new BigDecimal("5000.00"))
                .period(LocalDate.of(2024, 12, 1))
                .status(BudgetStatus.BUDGET_STATUS_INCOMPLETE)
                .build();

        when(budgetService.getCurrentBudget()).thenReturn(budgetDataDTO);

        mockMvc.perform(get("/api/budgets/current"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("getCurrentBudget",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @WithMockUser
    public void updateCurrentBudget_shouldReturn200Ok() throws Exception {
        BigDecimal amount = new BigDecimal("5000.00");
        CreateUpdateBudgetDTO createUpdateBudgetDTO = CreateUpdateBudgetDTO.builder()
                .amount(amount)
                .build();
        BudgetResponseDTO budgetResponseDTO = BudgetResponseDTO.builder()
                .budgetId(1)
                .build();

        when(budgetService.updateCurrentBudget(amount)).thenReturn(budgetResponseDTO);

        mockMvc.perform(put("/api/budgets/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateBudgetDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("updateCurrentBudget",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

}
