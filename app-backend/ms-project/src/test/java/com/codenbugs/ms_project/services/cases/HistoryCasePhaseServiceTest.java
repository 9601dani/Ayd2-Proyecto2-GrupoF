package com.codenbugs.ms_project.services.cases;

import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseWithCaseDto;
import com.codenbugs.ms_project.dtos.report.*;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import com.codenbugs.ms_project.repositories.cases.CasePhaseRepository;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.cases.HistoryCasePhaseRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import com.codenbugs.ms_project.repositories.typeCases.TypeCasesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HistoryCasePhaseServiceTest {
    @Mock
    private HistoryCasePhaseRepository historyCasePhaseRepository;

    @Mock
    private CasePhaseRepository casePhaseRepository;

    @Mock
    private TypeCasesRepository typeCasesRepository;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private UserRestClient userRestClient;

    @Mock
    private ProjectRepository projectRepository;
    private HistoryCasePhaseServiceImpl hisotryCasePhaseService;
    private HistoryCaseWithCaseDto dto;

    private final Integer ID_HISTORY = 1;
    private final Integer FK_CASE = 1;
    private final String PHASE_NAME = "Fase 1";
    private final Boolean IS_COMPLETED = false;
    private final BigDecimal TIME_SPENT = BigDecimal.TEN;
    private final Integer ID_CASE = 1;
    private final String CASE_NAME = "Caso 1";
    private final String DESCRIPTION = "Descripción";
    private final Integer FK_PROJECT = 1;
    private final BigDecimal PROGRESS_PERCENTAGE = BigDecimal.valueOf(80);
    private final Integer FK_CASE_TYPE = 1;
    private final LocalDateTime LIMIT_DATE = LocalDateTime.of(2025, 5, 1, 10, 30);
    private final LocalDateTime CREATED_AT = LocalDateTime.of(2025, 5, 1, 10, 30);
    private final Boolean IS_ENABLED = true;
    private final Boolean IS_CANCELLED = false;
    private final String REASON_CANCELLATION = "Motivo de cancelación";
    private final Integer FK_USER_ID = 1;
    private final Integer FK_CASE_PHASE = 1;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        hisotryCasePhaseService = new HistoryCasePhaseServiceImpl(historyCasePhaseRepository, casePhaseRepository, typeCasesRepository, caseRepository, userRestClient, projectRepository);

        dto = new HistoryCaseWithCaseDto(
                ID_HISTORY,
                FK_CASE,
                FK_USER_ID,
                FK_CASE_PHASE,
                IS_COMPLETED,
                TIME_SPENT,
                PHASE_NAME,
                FK_PROJECT,
                PROGRESS_PERCENTAGE,
                LIMIT_DATE,
                IS_ENABLED,
                IS_CANCELLED,
                CREATED_AT
        );
    }

    @Test
    void getAllWithCaseInfoReturnsList(){
        when(historyCasePhaseRepository.findAllWithCaseInfo()).thenReturn(List.of(dto));

        // Act
        List<HistoryCaseWithCaseDto> result = hisotryCasePhaseService.getAllWithCaseInfo();

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findByFkUser_ReturnsMatchingHistory() {
        // Arrange
        HistoryCasePhase history = new HistoryCasePhase();
        history.setId(ID_HISTORY);
        history.setFkCase(FK_CASE);
        history.setFkUser(FK_USER_ID);
        history.setFkCasePhase(FK_CASE_PHASE);
        history.setIsCompleted(IS_COMPLETED);
        history.setTimeSpent(TIME_SPENT);

        when(historyCasePhaseRepository.findByFkUser(FK_USER_ID)).thenReturn(List.of(history));

        // Act
        List<HistoryCasePhase> result = hisotryCasePhaseService.findByFkUser(FK_USER_ID);

        // Assert
        assertEquals(1, result.size());
        assertEquals(history, result.get(0));
    }

    @Test
    void getProjectUserHoursSummarySuccesfully() {
        // Arrange
        ProjectUserHoursDto dto = new ProjectUserHoursDto(
                FK_PROJECT,
                "Proyecto A",
                FK_USER_ID,
                BigDecimal.TEN
        );

        when(historyCasePhaseRepository.getProjectUserHoursSummary()).thenReturn(List.of(dto));

        // Act
        List<ProjectUserHoursDto> result = hisotryCasePhaseService.getProjectUserHoursSummary();

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getCaseTypeUserHoursReportSuccesfully() {
        // Arrange
        CaseTypeUserHoursDto dto = new CaseTypeUserHoursDto(
                FK_CASE_TYPE,
                "Tipo A",
                FK_USER_ID,
                BigDecimal.TEN
        );

        when(historyCasePhaseRepository.getCaseTypeUserHoursReport()).thenReturn(List.of(dto));

        // Act
        List<CaseTypeUserHoursDto> result = hisotryCasePhaseService.getCaseTypeUserHoursReport();

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getTopContributorSuccesfully() {
        TopContributorDto dto = new TopContributorDto(FK_USER_ID, 15L);

        when(historyCasePhaseRepository.getTopContributor()).thenReturn(dto);

        TopContributorDto result = hisotryCasePhaseService.getTopContributor();

        assertEquals(FK_USER_ID, result.userId());
        assertEquals(15L, result.totalCases());
    }


    @Test
    void getTopWorkerByHoursSuccesfully() {
        TopWorkerByHoursDto dto = new TopWorkerByHoursDto(FK_USER_ID, BigDecimal.valueOf(40));

        when(historyCasePhaseRepository.getTopWorkerByHours()).thenReturn(dto);

        TopWorkerByHoursDto result = hisotryCasePhaseService.getTopWorkerByHours();

        assertEquals(FK_USER_ID, result.userId());
        assertEquals(BigDecimal.valueOf(40), result.totalHours());
    }

    @Test
    void getCasesWithUserInfoSuccesfully() {
        CaseUserReportDto dto = new CaseUserReportDto(
                1,
                "Caso 1",
                "Descripción del caso",
                2,
                "Tipo A",
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(10),
                FK_USER_ID
        );

        when(historyCasePhaseRepository.findAllCasesWithUserInfo()).thenReturn(List.of(dto));

        List<CaseUserReportDto> result = hisotryCasePhaseService.getCasesWithUserInfo();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getUserTimeByDateSuccesfully() {
        UserTimeByDateDto dto = new UserTimeByDateDto(
                FK_USER_ID,
                LocalDateTime.of(2025, 5, 10, 8, 0),
                BigDecimal.valueOf(6)
        );

        when(historyCasePhaseRepository.getUserTimeGroupedByDate()).thenReturn(List.of(dto));

        List<UserTimeByDateDto> result = hisotryCasePhaseService.getUserTimeByDate();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

}
