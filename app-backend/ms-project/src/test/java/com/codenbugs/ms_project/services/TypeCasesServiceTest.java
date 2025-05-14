package com.codenbugs.ms_project.services;

import com.codenbugs.ms_project.dtos.cases.PhasesCaseRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesResponse;
import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.model.cases.TypeCase;
import com.codenbugs.ms_project.repositories.typeCases.TypeCasesRepository;
import com.codenbugs.ms_project.services.type_cases.PhaseCasesService;
import com.codenbugs.ms_project.services.type_cases.TypeCasesService;
import com.codenbugs.ms_project.services.type_cases.TypeCasesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TypeCasesServiceTest {

    private TypeCasesService typeCasesService;

    @Mock
    private TypeCasesRepository typeCasesRepository;

    @Mock
    private PhaseCasesService phaseCasesService;

    private final Integer TYPE_CASES_ID = 1;
    private final String TYPE_CASES_NAME = "type name";
    private final String TYPE_CASES_DESCRIPTION = "type description";

    private final Integer PHASE_ID = 1;
    private final String PHASE_NAME = "Phase 1";
    private final Integer CASE_TYPE_ID = 1;
    private final Integer NEXT_PHASE_ID = 1;

    private TypeCase typeCase;
    private CasePhase phasesCase;
    private List<CasePhase> phases;

    private TypeCasesRequest typeCasesRequest;
    private TypeCasesResponse typeCasesResponse;
    private PhasesCaseRequest phasesCaseRequest;

    private List<PhasesCaseRequest> phasesRequests;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        typeCasesService = new TypeCasesServiceImpl(typeCasesRepository, phaseCasesService);

        typeCase = new TypeCase();
        typeCase.setId(TYPE_CASES_ID);
        typeCase.setName(TYPE_CASES_NAME);
        typeCase.setDescription(TYPE_CASES_DESCRIPTION);

        phasesCase = new CasePhase();
        phasesCase.setId(PHASE_ID);
        phasesCase.setName(PHASE_NAME);
        phasesCase.setFkCaseType(CASE_TYPE_ID);
        phasesCase.setNextPhase(NEXT_PHASE_ID);

        phases = new ArrayList<>();
        phases.add(phasesCase);

        phasesCaseRequest = new PhasesCaseRequest(PHASE_NAME, NEXT_PHASE_ID);
        phasesRequests = new ArrayList<>();
        phasesRequests.add(phasesCaseRequest);

        typeCasesResponse = new TypeCasesResponse(TYPE_CASES_ID, TYPE_CASES_NAME, TYPE_CASES_DESCRIPTION, phases);
        typeCasesRequest = new TypeCasesRequest(TYPE_CASES_NAME, TYPE_CASES_DESCRIPTION, phasesRequests);
    }

    @Test
    public void getAllTypeCasesSuccessfully() {
        phasesCase.setNextPhase(null);

        when(typeCasesRepository.findAll()).thenReturn(List.of(typeCase));
        when(phaseCasesService.findByCaseType(TYPE_CASES_ID)).thenReturn(List.of(phasesCase));

        List<TypeCasesResponse> result = typeCasesService.getAllTypeCases();

        assertEquals(1, result.size());
        TypeCasesResponse response = result.get(0);
        assertEquals(TYPE_CASES_ID, response.id());
        assertEquals(TYPE_CASES_NAME, response.name());
        assertEquals(TYPE_CASES_DESCRIPTION, response.description());
        assertEquals(1, response.phases().size());
        assertEquals(PHASE_ID, response.phases().get(0).getId());
    }


}
