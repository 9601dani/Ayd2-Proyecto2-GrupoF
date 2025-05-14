package com.codenbugs.ms_project.services.type_cases;

import com.codenbugs.ms_project.dtos.cases.PhasesCaseRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesResponse;
import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.model.cases.TypeCase;
import com.codenbugs.ms_project.exceptions.typeCases.NameTypeCaseAlreadyExist;
import com.codenbugs.ms_project.exceptions.typeCases.TypeCaseNotFoundException;
import com.codenbugs.ms_project.repositories.typeCases.TypeCasesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    public void createTypeCaseWithExistingNameThrowsException() {
        // Arrange
        when(typeCasesRepository.findByName(TYPE_CASES_NAME)).thenReturn(typeCase);

        // Act & Assert
        assertThrows(NameTypeCaseAlreadyExist.class, () -> {
            typeCasesService.create(typeCasesRequest);
        });

        verify(typeCasesRepository, never()).save(any());
        verify(phaseCasesService, never()).save(any(), anyInt(), any());
    }

    @Test
    public void createTypeCaseFailsWhenPhaseSaveReturnsNull() {
        // Arrange
        when(typeCasesRepository.findByName(TYPE_CASES_NAME)).thenReturn(null);

        TypeCase savedType = new TypeCase();
        savedType.setId(TYPE_CASES_ID);
        savedType.setName(TYPE_CASES_NAME);
        savedType.setDescription(TYPE_CASES_DESCRIPTION);
        when(typeCasesRepository.save(any())).thenReturn(savedType);

        when(phaseCasesService.save(any(), eq(TYPE_CASES_ID), any())).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            typeCasesService.create(typeCasesRequest);
        });

        assertEquals("La fase no pudo ser guardada correctamente.", exception.getMessage());
    }

    @Test
    public void updateTypeCaseSuccessfully() throws Exception {
        when(typeCasesRepository.findById(TYPE_CASES_ID)).thenReturn(Optional.of(typeCase));
        when(typeCasesRepository.findByName(TYPE_CASES_NAME)).thenReturn(typeCase);
        when(phaseCasesService.save(any(), anyInt(), any())).thenReturn(phasesCase);

        TypeCasesResponse result = typeCasesService.update(TYPE_CASES_ID, typeCasesRequest);

        assertNotNull(result);
        assertEquals(TYPE_CASES_NAME, result.name());
    }

    @Test
    public void updateTypeCaseNotFound() {
        when(typeCasesRepository.findById(TYPE_CASES_ID)).thenReturn(Optional.empty());

        assertThrows(TypeCaseNotFoundException.class, () -> {
            typeCasesService.update(TYPE_CASES_ID, typeCasesRequest);
        });
    }

    @Test
    public void updateTypeCaseWithDuplicateNameThrowsException() {
        TypeCase otherTypeCase = new TypeCase();
        otherTypeCase.setId(999);
        otherTypeCase.setName(TYPE_CASES_NAME);

        when(typeCasesRepository.findById(TYPE_CASES_ID)).thenReturn(Optional.of(typeCase));
        when(typeCasesRepository.findByName(TYPE_CASES_NAME)).thenReturn(otherTypeCase);

        assertThrows(NameTypeCaseAlreadyExist.class, () -> {
            typeCasesService.update(TYPE_CASES_ID, typeCasesRequest);
        });
    }






}
