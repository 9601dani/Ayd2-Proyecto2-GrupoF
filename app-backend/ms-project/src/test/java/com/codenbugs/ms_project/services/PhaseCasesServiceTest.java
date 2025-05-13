package com.codenbugs.ms_project.services;

import com.codenbugs.ms_project.dtos.cases.PhasesCaseRequest;
import com.codenbugs.ms_project.model.cases.PhasesCase;
import com.codenbugs.ms_project.repositories.typeCases.PhaseCasesRepository;
import com.codenbugs.ms_project.services.type_cases.PhaseCasesService;
import com.codenbugs.ms_project.services.type_cases.PhaseCasesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PhaseCasesServiceTest {

    private PhaseCasesService phaseCasesService;

    @Mock
    private PhaseCasesRepository phaseCasesRepository;

    private final Integer PHASE_ID = 1;
    private final String PHASE_NAME = "Phase 1";
    private final Integer CASE_TYPE_ID = 1;
    private final Integer NEXT_PHASE_ID = 1;

    private PhasesCase phasesCase;
    private PhasesCaseRequest phasesCaseRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        phaseCasesService = new PhaseCasesServiceImpl(phaseCasesRepository);

        phasesCase = new PhasesCase();
        phasesCase.setId(PHASE_ID);
        phasesCase.setName(PHASE_NAME);
        phasesCase.setFkCaseType(CASE_TYPE_ID);
        phasesCase.setNextPhase(NEXT_PHASE_ID);

        phasesCaseRequest = new PhasesCaseRequest(PHASE_NAME, NEXT_PHASE_ID);
    }

    @Test
    public void findByCaseTypeReturnsPhasesSuccessfully() {
        List<PhasesCase> expected = List.of(phasesCase);

        when(phaseCasesRepository.findByFkCaseType(CASE_TYPE_ID)).thenReturn(expected);

        List<PhasesCase> actual = phaseCasesService.findByCaseType(CASE_TYPE_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void savePhaseCaseSuccessfully() {

        when(phaseCasesRepository.save(any(PhasesCase.class))).thenReturn(phasesCase);

        PhasesCase actual = phaseCasesService.save(phasesCaseRequest, CASE_TYPE_ID, NEXT_PHASE_ID);

        assertEquals(phasesCase.getFkCaseType(), actual.getFkCaseType());
        assertEquals(phasesCase.getName(), actual.getName());
        assertEquals(phasesCase.getNextPhase(), actual.getNextPhase());
    }

    @Test
    public void deleteAllByFKCaseType_Successfully() {
        phaseCasesService.deleteAllByFKCaseType(CASE_TYPE_ID);

        verify(phaseCasesRepository, times(1)).deleteByFkCaseType(CASE_TYPE_ID);
    }




}
