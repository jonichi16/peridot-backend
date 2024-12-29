package com.jonichi.peridot.envelope.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonichi.peridot.auth.config.SecurityConfig;
import com.jonichi.peridot.auth.repository.UserRepository;
import com.jonichi.peridot.budget.repository.BudgetRepository;
import com.jonichi.peridot.envelope.dto.CreateUpdateEnvelopeDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeDataDTO;
import com.jonichi.peridot.envelope.dto.EnvelopeResponseDTO;
import com.jonichi.peridot.envelope.dto.PeridotPagination;
import com.jonichi.peridot.envelope.model.BudgetEnvelopeStatus;
import com.jonichi.peridot.envelope.service.EnvelopeService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Test
    @WithMockUser
    public void getEnvelopes_shouldReturn200Ok() throws Exception {
        // given
        Integer budgetId = 1;
        Integer page = 1;
        Integer size = 10;
        String sortBy = "id";
        String sortDirection = "DESC";

        List<EnvelopeDataDTO> envelopes = List.of(
                EnvelopeDataDTO.builder()
                        .name("Sample 1")
                        .description("This is sample envelope")
                        .amount(BigDecimal.valueOf(1000))
                        .recurring(true)
                        .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                        .build(),
                EnvelopeDataDTO.builder()
                        .name("Sample 2")
                        .description("This is sample envelope")
                        .amount(BigDecimal.valueOf(500))
                        .recurring(true)
                        .status(BudgetEnvelopeStatus.ENVELOPE_STATUS_UNDER)
                        .build()
        );

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<EnvelopeDataDTO> pagination = new PageImpl<>(envelopes, pageable, envelopes.size());

        PeridotPagination<EnvelopeDataDTO> peridotPagination = PeridotPagination.<EnvelopeDataDTO>builder()
                .content(envelopes)
                .totalPages(pagination.getTotalPages())
                .totalElements(pagination.getTotalElements())
                .numberOfElements(pagination.getNumberOfElements())
                .currentPage(pagination.getNumber() + 1)
                .first(pagination.isFirst())
                .last(pagination.isLast())
                .build();

        // when
        when(envelopeService.getEnvelopes(
                budgetId,
                page,
                size,
                sortBy,
                sortDirection
        )).thenReturn(peridotPagination);

        // then
        mockMvc.perform(get("/api/budgets/1/envelopes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("getEnvelopes",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

}
