package com.jonichi.peridot.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonichi.peridot.auth.config.SecurityConfig;
import com.jonichi.peridot.auth.repository.UserRepository;
import com.jonichi.peridot.budget.dto.CreateBudgetDTO;
import com.jonichi.peridot.budget.service.BudgetService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class BudgetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BudgetService budgetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void createBudget_shouldReturn201Created() throws Exception {
        // given
        CreateBudgetDTO createBudgetDTO = CreateBudgetDTO.builder()
                .amount(new BigDecimal("1000"))
                .build();

        // when

        // then
        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBudgetDTO)))
                .andExpect(status().isCreated());
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
                .andExpect(status().isBadRequest());
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
                .andExpect(status().isBadRequest());
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
        CreateBudgetDTO createBudgetDTO = new CreateBudgetDTO(new BigDecimal("1000"));

        // when

        // then
        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBudgetDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void getCurrentBudget_shouldReturn200Ok() throws Exception {
        mockMvc.perform(get("/api/budgets/current"))
                .andExpect(status().isOk());
    }

}
