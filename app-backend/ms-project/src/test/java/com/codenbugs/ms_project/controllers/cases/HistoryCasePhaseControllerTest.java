package com.codenbugs.ms_project.controllers.cases;

import com.codenbugs.ms_project.dtos.cases.*;
import com.codenbugs.ms_project.services.cases.HistoryCasePhaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoryCasePhaseController.class)
public class HistoryCasePhaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HistoryCasePhaseService historyCasePhaseService;

    private HistoryCaseWithCaseDto historyDto;
    private final Integer ID = 1;
    private final Integer FK_CASE = 2;
    private final Integer FK_NEXT = 3;
    private final Integer FK_USER = 3;
    private final Integer FK_CASE_PHASE = 4;
    private final Boolean IS_COMPLETED = true;
    private final BigDecimal TIME_SPENT = BigDecimal.valueOf(8.5);
    private final String PHASE_NAME = "Fase 1";
    private final Integer FK_PROJECT = 5;
    private final BigDecimal PROGRESS_PERCENTAGE = BigDecimal.valueOf(75.0);
    private final LocalDateTime LIMIT_DATE = LocalDateTime.now().plusDays(5);
    private final Boolean IS_ENABLED = true;
    private final Boolean IS_CANCELLED = false;
    private final LocalDateTime CREATED_AT = LocalDateTime.now();


    @BeforeEach
    void setUp() {
        historyDto = new HistoryCaseWithCaseDto(
                ID, FK_CASE, FK_USER, FK_CASE_PHASE, IS_COMPLETED,
                TIME_SPENT, PHASE_NAME, FK_PROJECT, PROGRESS_PERCENTAGE,
                LIMIT_DATE, IS_ENABLED, IS_CANCELLED, CREATED_AT
        );
    }

    @Test
    void getAllWithCaseInfo() throws Exception {
        List<HistoryCaseWithCaseDto> list = List.of(historyDto);
        when(historyCasePhaseService.getAllWithCaseInfo()).thenReturn(list);

        mockMvc.perform(get("/v1/histories/all-cases")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @Test
    void getNextPhaseSuccessfully() throws Exception {
        CasePhaseResponse response = new CasePhaseResponse(1, "Fase siguiente", "Tipo A");

        when(historyCasePhaseService.getNextPhase(ID)).thenReturn(response);

        mockMvc.perform(get("/v1/histories/next/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void updateCasePhaseSuccessfully() throws Exception {
        HistoryCaseRequest request = new HistoryCaseRequest(ID,IS_COMPLETED, TIME_SPENT);
        HistoryCaseResponseDto response = new HistoryCaseResponseDto(ID, FK_CASE, FK_USER, FK_CASE_PHASE, IS_COMPLETED, TIME_SPENT);

        when(historyCasePhaseService.updateCasePhase(request)).thenReturn(response);

        mockMvc.perform(put("/v1/histories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void saveNextPhaseSuccessfully() throws Exception {
        NextPhaseRequest request = new NextPhaseRequest(FK_CASE,FK_NEXT, FK_USER);
        HistoryCaseResponseDto response = new HistoryCaseResponseDto(ID, FK_CASE, FK_USER, FK_CASE_PHASE, IS_COMPLETED, TIME_SPENT);

        when(historyCasePhaseService.saveNextPhase(request)).thenReturn(response);

        mockMvc.perform(post("/v1/histories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void completeCaseSuccessfully() throws Exception {
        mockMvc.perform(put("/v1/histories/complete/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



}