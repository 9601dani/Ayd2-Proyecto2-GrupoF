package com.codenbugs.ms_project.services.cases;

import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.cases.CaseCancelledRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseResponseDto;
import com.codenbugs.ms_project.dtos.cases.CaseWithUserDto;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserIsDisabled;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.model.project.Project;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.cases.HistoryCasePhaseRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import com.codenbugs.ms_project.repositories.typeCases.PhaseCasesRepository;
import com.codenbugs.ms_project.repositories.typeCases.TypeCasesRepository;
import com.codenbugs.ms_project.services.cases.CaseService;
import com.codenbugs.ms_project.services.cases.CaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class CaseServiceTest {

    private CaseService caseService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private UserRestClient userRestClient;

    @Mock
    private HistoryCasePhaseRepository historyCasePhaseRepository;

    @Mock
    private PhaseCasesRepository phaseCasesRepository;

    @Mock
    private TypeCasesRepository typeCasesRepository;

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
    private final LocalDateTime CREATED_AT = LocalDateTime.now();

    private final Integer USER_ID = 1;
    private final String USERNAME = "test username";
    private final Integer ROLE_ID = 1;
    private final String PHOTO = "test photo";
    private final BigDecimal SALARY = BigDecimal.valueOf(50.67);
    private final Boolean USER_IS_ENABLED = true;

    private Case testCase;
    private CaseRequestDto request;
    private CaseCancelledRequestDto requestCancelled;
    private CaseResponseDto response;
    private Project project;
    private UserResponse user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        caseService = new CaseServiceImpl(caseRepository, historyCasePhaseRepository, phaseCasesRepository, projectRepository, userRestClient, typeCasesRepository);

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
        testCase.setCreatedAt(CREATED_AT);

        request = new CaseRequestDto(ID, PROJECT_ID, CASE_TYPE, USER_ID, LIMIT_DATE, NAME, DESCRIPTION, CREATED_AT);

        requestCancelled = new CaseCancelledRequestDto(ID, REASON_CANCELLATION);

        response = new CaseResponseDto(ID, PROJECT_ID, PROGRESS_PERCENTAGE, CASE_TYPE, LIMIT_DATE, IS_ENABLED, NAME, DESCRIPTION, IS_CANCELED, REASON_CANCELLATION, CREATED_AT);

        project = new Project();
        project.setId(PROJECT_ID);
        project.setIsEnabled(IS_ENABLED);

        user = new UserResponse(USER_ID, USERNAME, ROLE_ID, PHOTO, SALARY, USER_IS_ENABLED);
    }

    @Test
    public void saveCaseSuccessfully() throws Exception {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        when(userRestClient.findById(USER_ID)).thenReturn(user);

        when(caseRepository.save(any())).thenReturn(testCase);

        CasePhase firstPhase = new CasePhase();
        firstPhase.setId(1);
        List<CasePhase> phases = new ArrayList<>();
        phases.add(firstPhase);

        when(phaseCasesRepository.findByFkCaseType(CASE_TYPE)).thenReturn(phases);

        CaseResponseDto actual = caseService.saveCase(request);

        assertEquals(response, actual);
    }

    @Test
    public void saveCaseProjectNotFound() {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> caseService.saveCase(request));
    }

    @Test
    public void saveCaseProjectIsDisabled() {
        project.setIsEnabled(false);
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        assertThrows(ProjectIsDisabled.class, () -> caseService.saveCase(request));
    }

    @Test
    public void saveCaseUserNotFound() throws UserNotFoundException {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        when(userRestClient.findById(USER_ID)).thenThrow(new UserNotFoundException("Usuario no encontrado"));

        assertThrows(UserNotFoundException.class, () -> caseService.saveCase(request));
    }

    @Test
    public void saveCaseUserIsDisabled() throws UserNotFoundException {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        user = new UserResponse(USER_ID, USERNAME, ROLE_ID, PHOTO, SALARY, false);
        when(userRestClient.findById(USER_ID)).thenReturn(user);

        assertThrows(UserIsDisabled.class, () -> caseService.saveCase(request));
    }

    @Test
    public void getCaseByIdSuccessfully() throws CaseNotFoundException {
        when(caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        CaseResponseDto actual = caseService.getCaseById(ID);

        assertEquals(response, actual);
    }

    @Test
    public void getCaseByIdNotFound() {
        when(caseRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CaseNotFoundException.class, () -> caseService.getCaseById(ID));
    }


    @Test
    public void updateCaseSuccessfullyWithoutTypeChange() throws CaseIsDisabled, CaseNotFoundException {
        when(caseRepository.findById(ID)).thenReturn(Optional.of(testCase));
        when(caseRepository.save(any())).thenReturn(testCase);

        CaseResponseDto actual = caseService.updateCase(request);

        assertEquals(response, actual);
        verify(historyCasePhaseRepository, never()).deleteAllHistoryCasePhaseByFkCase(any());
        verify(historyCasePhaseRepository, never()).save(any());
    }


    @Test
    public void updateCaseSuccessfullyWithTypeChange() throws CaseIsDisabled, CaseNotFoundException {
        testCase.setFK_Case_Type(999);

        when(caseRepository.findById(ID)).thenReturn(Optional.of(testCase));
        when(caseRepository.save(any())).thenReturn(testCase);

        CasePhase newPhase = new CasePhase();
        newPhase.setId(10);
        when(phaseCasesRepository.findByFkCaseType(CASE_TYPE)).thenReturn(List.of(newPhase));

        CaseResponseDto actual = caseService.updateCase(request);

        assertEquals(response, actual);
        verify(historyCasePhaseRepository).deleteAllHistoryCasePhaseByFkCase(ID);
        verify(historyCasePhaseRepository).save(any());
    }

    @Test
    public void updateCaseNotFound() {
        when(caseRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CaseNotFoundException.class, () -> caseService.updateCase(request));
    }

    @Test
    public void updateCaseIsDisabled() {
        testCase.setIsEnabled(false);
        when(caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        assertThrows(CaseIsDisabled.class, () -> caseService.updateCase(request));
    }

    @Test
    public void updateCaseIsCancelled() {
        testCase.setIsCancelled(true);
        when(caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        assertThrows(CaseIsDisabled.class, () -> caseService.updateCase(request));
    }

    @Test
    public void cancelCaseSuccessfully() throws CaseNotFoundException, CaseIsDisabled {
        CaseResponseDto expected = new CaseResponseDto(
                ID, PROJECT_ID, PROGRESS_PERCENTAGE, CASE_TYPE, LIMIT_DATE, IS_ENABLED,
                NAME, DESCRIPTION, true, REASON_CANCELLATION, CREATED_AT
        );

        when(caseRepository.findById(ID)).thenReturn(Optional.of(testCase));
        when(caseRepository.save(any())).thenReturn(testCase);

        CaseResponseDto actual = caseService.cancelCase(requestCancelled);

        assertEquals(expected, actual);
    }

    @Test
    public void cancelCaseNotFound() {
        when(caseRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CaseNotFoundException.class, () -> caseService.cancelCase(requestCancelled));
    }

    @Test
    public void cancelCaseAlreadyCancelled() {
        testCase.setIsCancelled(true);

        when(caseRepository.findById(ID)).thenReturn(Optional.of(testCase));

        assertThrows(CaseIsDisabled.class, () -> caseService.cancelCase(requestCancelled));
    }

    @Test
    public void getCasesByProjectIdSuccessfully() {
        List<Case> cases = List.of(testCase);

        when(caseRepository.findByFkProject(PROJECT_ID)).thenReturn(cases);

        List<CaseResponseDto> expected = List.of(response);
        List<CaseResponseDto> actual = caseService.getCasesByProjectId(PROJECT_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void getActiveCasesByProjectSuccessfully() {
        CaseWithUserDto dto = new CaseWithUserDto(
                ID, NAME, DESCRIPTION, PROJECT_ID,
                PROGRESS_PERCENTAGE, CASE_TYPE, USER_ID,
                LIMIT_DATE, IS_ENABLED, IS_CANCELED, REASON_CANCELLATION
        );

        List<CaseWithUserDto> expected = List.of(dto);

        when(caseRepository.findAllEnabledNotCancelledCasesByProject(PROJECT_ID)).thenReturn(expected);

        List<CaseWithUserDto> actual = caseService.getActiveCasesByProject(PROJECT_ID);

        assertEquals(expected, actual);
    }

    @Test
    public void getCasesByIsCancelledSuccessfully() {
        testCase.setIsCancelled(true);
        response = new CaseResponseDto(testCase);

        when(caseRepository.findByIsCancelled(true)).thenReturn(List.of(testCase));

        List<CaseResponseDto> expected = List.of(response);
        List<CaseResponseDto> actual = caseService.getCasesByIsCancelled(true);

        assertEquals(expected, actual);
    }

    @Test
    public void saveCaseWithNoPhasesFails() throws UserNotFoundException {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(userRestClient.findById(USER_ID)).thenReturn(user);
        when(phaseCasesRepository.findByFkCaseType(CASE_TYPE)).thenReturn(new ArrayList<>());

        assertThrows(IndexOutOfBoundsException.class, () -> caseService.saveCase(request));
    }



}
