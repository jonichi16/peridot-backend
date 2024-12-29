package com.jonichi.peridot.envelope.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonichi.peridot.auth.config.SecurityConfig;
import com.jonichi.peridot.auth.repository.UserRepository;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.envelope.dto.CreateUpdateEnvelopeDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.service.EnvelopeService;
import java.math.BigDecimal;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class EnvelopeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BudgetRepository budgetRepository;
    @MockBean
    private EnvelopeService envelopeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void createEnvelope_shouldReturn201Created() throws Exception {
        // given
        BigDecimal amount = new BigDecimal("500.00");
        CreateUpdateEnvelopeDTO createUpdateEnvelopeDTO = CreateUpdateEnvelopeDTO.builder()
                .name("Sample")
                .description("This is a sample envelope")
                .amount(amount)
                .recurring(true)
                .build();
        EnvelopeResponseDTO envelopeResponseDTO = EnvelopeResponseDTO.builder()
                .envelopeId(1)
                .budgetEnvelopeId(1)
                .build();

        // when
        when(envelopeService.createEnvelope(
                createUpdateEnvelopeDTO.name(),
                createUpdateEnvelopeDTO.description(),
                createUpdateEnvelopeDTO.amount(),
                createUpdateEnvelopeDTO.recurring()
        )).thenReturn(envelopeResponseDTO);

        // then
        mockMvc.perform(post("/api/envelopes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateEnvelopeDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(
                        document("createEnvelopeSuccess",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @WithMockUser
    public void createEnvelope_withMissingFields_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ }";

        // when

        // then
        mockMvc.perform(post("/api/envelopes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("createEnvelopesMissingFields",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @WithMockUser
    public void createEnvelope_withLessThanZeroAmount_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ \"amount\": \"-1.00\" }";

        // when

        // then
        mockMvc.perform(post("/api/envelopes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("createEnvelopeInvalidRequest",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @WithMockUser
    public void updateEnvelope_shouldReturn200Ok() throws Exception {
        // given
        CreateUpdateEnvelopeDTO createUpdateEnvelopeDTO = CreateUpdateEnvelopeDTO.builder()
                .name("Sample")
                .description("This is sample")
                .amount(BigDecimal.valueOf(1000))
                .recurring(true)
                .build();
        EnvelopeResponseDTO envelopeResponseDTO = EnvelopeResponseDTO.builder()
                .envelopeId(1)
                .budgetEnvelopeId(1)
                .build();

        // when
        when(envelopeService.createEnvelope(
                createUpdateEnvelopeDTO.name(),
                createUpdateEnvelopeDTO.description(),
                createUpdateEnvelopeDTO.amount(),
                createUpdateEnvelopeDTO.recurring()
        )).thenReturn(envelopeResponseDTO);

        // then /api/envelopes/{envelopeId} - envelopeId = budgetEnvelopeId
        mockMvc.perform(put("/api/envelopes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateEnvelopeDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("updateEnvelopeSuccess",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @WithMockUser
    public void updateEnvelope_withMissingFields_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ }";

        // when

        // then
        mockMvc.perform(put("/api/envelopes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("updateEnvelopesMissingFields",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @WithMockUser
    public void updateEnvelope_withLessThanZeroAmount_shouldReturn400BadRequestError() throws Exception {
        // given
        String invalidRequest = "{ \"amount\": \"-1.00\" }";

        // when

        // then
        mockMvc.perform(put("/api/envelopes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("updateEnvelopeInvalidRequest",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

}
