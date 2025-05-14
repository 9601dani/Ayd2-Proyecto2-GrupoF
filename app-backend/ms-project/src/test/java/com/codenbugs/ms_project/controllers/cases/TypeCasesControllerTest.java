package com.codenbugs.ms_project.controllers.cases;

import com.codenbugs.ms_project.dtos.cases.TypeCasesRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesResponse;
import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.model.cases.TypeCase;
import com.codenbugs.ms_project.services.type_cases.TypeCasesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TypeCasesController.class)
public class TypeCasesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TypeCasesService typeCasesService;

    private final Integer TYPE_ID = 1;
    private final String NAME = "Tipo Caso";
    private final String DESCRIPTION = "Descripción del tipo de caso";
    private List<CasePhase> PHASES;

    private TypeCasesRequest request;
    private TypeCasesResponse response;

    @BeforeEach
    void setUp() {
        PHASES = List.of(new CasePhase());
        request = new TypeCasesRequest(NAME, DESCRIPTION, List.of());

        TypeCase typesCase = new TypeCase();
        typesCase.setId(TYPE_ID);
        typesCase.setName(NAME);
        typesCase.setDescription(DESCRIPTION);

        response = new TypeCasesResponse(typesCase, PHASES);
    }

    @Test
    void getAllCases() throws Exception {
        when(typeCasesService.getAllTypeCases()).thenReturn(List.of(response));

        mockMvc.perform(get("/v1/type_cases"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(response))));
    }

    @Test
    void saveNewCase() throws Exception {
        when(typeCasesService.create(any(TypeCasesRequest.class))).thenReturn(response);

        mockMvc.perform(post("/v1/type_cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void updateCase() throws Exception {
        when(typeCasesService.update(eq(TYPE_ID), any(TypeCasesRequest.class))).thenReturn(response);

        mockMvc.perform(put("/v1/type_cases/{id}", TYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
