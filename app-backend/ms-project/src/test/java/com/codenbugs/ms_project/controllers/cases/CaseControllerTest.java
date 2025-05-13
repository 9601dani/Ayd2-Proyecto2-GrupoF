package com.codenbugs.ms_project.controllers.cases;

import com.codenbugs.ms_project.dtos.cases.CaseCancelledRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseResponseDto;
import com.codenbugs.ms_project.dtos.cases.CaseWithUserDto;
import com.codenbugs.ms_project.services.cases.CaseService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CaseController.class)
public class CaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CaseService caseService;

    private final Integer CASE_ID = 1;
    private final Integer FK_PROJECT = 2;
    private final Integer USER_ID = 3;
    private final Integer FK_CASE_TYPE = 1;
    private final String NAME = "Caso A";
    private final String DESCRIPTION = "Descripción caso A";
    private final BigDecimal PROGRESS_PERCENTAGE = BigDecimal.valueOf(100);
    private final Boolean IS_CANCELLED = false;
    private final Boolean ENABLED = true;
    private final String CANCEL_REASON = "Ya no es necesario";
    private final LocalDateTime CREATED = LocalDateTime.now();

    private CaseRequestDto caseRequestDto;
    private CaseResponseDto caseResponseDto;
    private CaseCancelledRequestDto caseCancelledRequestDto;

    @BeforeEach
    void setUp() {
        caseRequestDto = new CaseRequestDto(CASE_ID,FK_PROJECT,FK_CASE_TYPE,USER_ID, CREATED,NAME, DESCRIPTION, CREATED);
        caseResponseDto = new CaseResponseDto(CASE_ID,FK_PROJECT,PROGRESS_PERCENTAGE,FK_CASE_TYPE,CREATED,ENABLED,NAME, DESCRIPTION,IS_CANCELLED,CANCEL_REASON);
        caseCancelledRequestDto = new CaseCancelledRequestDto(CASE_ID, CANCEL_REASON);
    }

    @Test
    void createCase() throws Exception {
        when(caseService.saveCase(any())).thenReturn(caseResponseDto);

        mockMvc.perform(post("/v1/cases/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caseRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(caseResponseDto)));
    }

    @Test
    void getCaseById() throws Exception {
        when(caseService.getCaseById(CASE_ID)).thenReturn(caseResponseDto);

        mockMvc.perform(get("/v1/cases/{id}", CASE_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(caseResponseDto)));
    }

    @Test
    void updateCase() throws Exception {
        when(caseService.updateCase(any())).thenReturn(caseResponseDto);

        mockMvc.perform(put("/v1/cases/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caseRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(caseResponseDto)));
    }

    @Test
    void updateCancelCase() throws Exception {
        when(caseService.cancelCase(any())).thenReturn(caseResponseDto);

        mockMvc.perform(put("/v1/cases/update/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caseCancelledRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(caseResponseDto)));
    }

    @Test
    void getAllCasesByFkProject() throws Exception {
        List<CaseResponseDto> list = List.of(caseResponseDto);
        when(caseService.getCasesByProjectId(FK_PROJECT)).thenReturn(list);

        mockMvc.perform(get("/v1/cases/all/{fkProject}", FK_PROJECT))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @Test
    void getCasesByIsCancelled() throws Exception {
        List<CaseResponseDto> list = List.of(caseResponseDto);
        when(caseService.getCasesByIsCancelled(IS_CANCELLED)).thenReturn(list);

        mockMvc.perform(get("/v1/cases/cancel/{isCancelled}", IS_CANCELLED))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @Test
    void getActiveCasesByProject() throws Exception {
        CaseWithUserDto dto = new CaseWithUserDto(CASE_ID, NAME, DESCRIPTION, FK_PROJECT, PROGRESS_PERCENTAGE,FK_CASE_TYPE,USER_ID,CREATED,ENABLED,IS_CANCELLED,CANCEL_REASON);
        List<CaseWithUserDto> list = List.of(dto);
        when(caseService.getActiveCasesByProject(FK_PROJECT)).thenReturn(list);

        mockMvc.perform(get("/v1/cases/active/{projectId}", FK_PROJECT))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }
}
