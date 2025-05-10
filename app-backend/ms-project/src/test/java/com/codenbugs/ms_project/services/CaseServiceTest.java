package com.codenbugs.ms_project.services;

import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.cases.CaseCancelledRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseResponseDto;
import com.codenbugs.ms_project.exceptions.cases.CaseException;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFound;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFound;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.project.Project;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import com.codenbugs.ms_project.services.cases.CaseService;
import com.codenbugs.ms_project.services.cases.CaseServiceImpl;
import com.codenbugs.ms_project.services.project.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class CaseServiceTest {

    private CaseService caseService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private CaseRepository caseRepository;

    private final Integer ID = 1;
    private final Integer PROJECT_ID = 1;
    private final BigDecimal PROGRESS_PERCENTAGE = BigDecimal.valueOf(50.67);
    private final Integer CASE_TYPE = 1;
    private final LocalDateTime LIMIT_DATE = LocalDateTime.of(2020, 1, 1, 1, 1);
    private final Boolean IS_ENABLED = true;
    private final String NAME = "Test Case";
    private final String DESCRIPTION = "Description Case";
    private final Boolean IS_CANCELED = false;
    private final String REASON_CANCELLATION = "Reason...";

    private Case testCase;
    private CaseRequestDto request;
    private CaseCancelledRequestDto requestCancelled;
    private CaseResponseDto response;
    private Project project;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        caseService = new CaseServiceImpl(caseRepository, projectRepository);

        testCase = new Case();
        testCase.setId(ID);
        testCase.setName(NAME);
        testCase.setDescription(DESCRIPTION);
        testCase.setProgressPercentage(PROGRESS_PERCENTAGE);
        testCase.setFkProject(PROJECT_ID);
        testCase.setFK_Case_Type(CASE_TYPE);
        testCase.setLimitDate(LIMIT_DATE);
        testCase.setIsEnabled(IS_ENABLED);
        testCase.setIsCancelled(IS_CANCELED);
        testCase.setReasonCancellation(REASON_CANCELLATION);

        request = new CaseRequestDto(ID, PROJECT_ID, CASE_TYPE, LIMIT_DATE, NAME, DESCRIPTION);

        requestCancelled = new CaseCancelledRequestDto(ID, REASON_CANCELLATION);

        response = new CaseResponseDto(ID, PROJECT_ID, PROGRESS_PERCENTAGE, CASE_TYPE, LIMIT_DATE, IS_ENABLED, NAME, DESCRIPTION, IS_CANCELED, REASON_CANCELLATION);

        project = new Project();
        project.setId(PROJECT_ID);
        project.setIsEnabled(IS_ENABLED);
    }

    @Test
    public void saveCaseSuccessfully() throws ProjectIsDisabled, ProjectNotFound {

        when(this.projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        when(this.caseRepository.save(any())).thenReturn(testCase);

        CaseResponseDto actual = this.caseService.saveCase(request);

        assertEquals(response, actual);

    }

    @Test
    public void saveCaseNotFound() throws ProjectIsDisabled, ProjectNotFound {

        when(this.projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFound.class, () -> this.caseService.saveCase(request));
    }

    @Test
    public void saveCaseIsDisabled() throws ProjectIsDisabled, ProjectNotFound {

        project.setIsEnabled(false);
        when(this.projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        assertThrows(ProjectIsDisabled.class, () -> this.caseService.saveCase(request));

    }

    @Test
    public void getCasesByIdSuccesfully() throws CaseNotFound {

        when(this.caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        CaseResponseDto actual = this.caseService.getCaseById(ID);

        assertEquals(response, actual);

    }

    @Test
    public void getCasesBYIdNotFound() throws CaseNotFound {
        when(this.caseRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(CaseNotFound.class, () -> this.caseService.getCaseById(ID));
    }

    @Test
    public void updateCasesSuccesfully() throws CaseIsDisabled, CaseNotFound {

        when(this.caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        when(this.caseRepository.save(any())).thenReturn(testCase);

        CaseResponseDto actual = this.caseService.updateCase(request);

        assertEquals(response, actual);

    }

    @Test
    public void updateCasesNotFound() throws CaseIsDisabled, CaseNotFound {

        when(this.caseRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CaseNotFound.class, () -> this.caseService.updateCase(request));
    }

    @Test
    public void updateCaseIsDisabled() throws CaseIsDisabled, CaseNotFound {

        testCase.setIsEnabled(false);

        when(this.caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        assertThrows(CaseIsDisabled.class, () -> this.caseService.updateCase(request));
    }

    @Test
    public void updateCaseIsCancelled(){

        testCase.setIsCancelled(true);

        when(this.caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        assertThrows(CaseIsDisabled.class, () -> this.caseService.updateCase(request));
    }

    @Test
    public void cancelCaseSuccesfully() throws CaseIsDisabled, CaseNotFound {

        CaseResponseDto expect = new CaseResponseDto(ID, PROJECT_ID, PROGRESS_PERCENTAGE, CASE_TYPE, LIMIT_DATE, IS_ENABLED, NAME, DESCRIPTION, true, REASON_CANCELLATION);

        when(this.caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        when(this.caseRepository.save(any())).thenReturn(testCase);

        CaseResponseDto actual = this.caseService.cancelCase(requestCancelled);

        assertEquals(expect, actual);
    }

    @Test
    public void cancelCaseNotFound() throws CaseIsDisabled, CaseNotFound {

        when(this.caseRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CaseNotFound.class, () -> this.caseService.cancelCase(requestCancelled));
    }

    @Test
    public void cancelCaseIsDisabled() throws CaseIsDisabled, CaseNotFound {
        testCase.setIsCancelled(true);
        when(this.caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        assertThrows(CaseIsDisabled.class, () -> this.caseService.cancelCase(requestCancelled));
    }

    @Test
    public void getCaseByFkProjectIdSuccesfully() throws ProjectIsDisabled, ProjectNotFound {

        List<Case> cases = new ArrayList<>();
        cases.add(testCase);

        when(this.caseRepository.findByFkProject(PROJECT_ID)).thenReturn(cases);

        List<CaseResponseDto> expected = new ArrayList<>();
        expected.add(response);

        List<CaseResponseDto> actual = this.caseService.getCasesByProjectId(PROJECT_ID);

        assertEquals(expected, actual);
    }
}
