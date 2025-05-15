package com.codenbugs.ms_report.services;

import com.codenbugs.ms_report.clients.HistoryRestClient;
import com.codenbugs.ms_report.clients.ProjectRestClient;
import com.codenbugs.ms_report.clients.UserRestClient;
import com.codenbugs.ms_report.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_report.dtos.report.*;
import com.codenbugs.ms_report.dtos.user.UserResponse;
import com.codenbugs.ms_report.dtos.user.UserResponseWithName;
import com.codenbugs.ms_report.dtos.utils.*;
import com.codenbugs.ms_report.exceptions.user.UserNotFoundException;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class ReportServiceTest {

    private ReportService reportService;

    @Mock
    private ProjectRestClient projectRestClient;

    @Mock
    private HistoryRestClient historyRestClient;

    @Mock
    private UserRestClient userRestClient;

    private final Integer PROJECT_ID = 1;
    private final String PROJECT_NAME = "Project Name";
    private final String PROJECT_DESCRIPTION = "Project Description";
    private final Boolean PROJECT_IS_ENABLED = true;
    private final Long CASE_COUNT = 5L;
    private final BigDecimal TOTAL_HOURS = new BigDecimal(24);
    private final BigDecimal TOTAL_INVESTED = new BigDecimal(100.50);
    private final Integer USER_ID = 1;
    private final String USER_NAME = "User Name";
    private final Integer  ROLE_ID = 1;
    private final String PHOTO = "Photo";
    private final BigDecimal USER_SALARY = new BigDecimal(55);

    private final Integer CASE_TYPE_ID = 1;
    private final String CASE_TYPE_NAME = "Case Type";

    private Report1Dto report1Dto;
    private Report2Dto report2Dto;
    private ProjectUserHoursDto projectUserHoursDto;
    private UserResponse userResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        reportService = new ReportServiceImpl(projectRestClient, historyRestClient, userRestClient);

        report1Dto = new Report1Dto(PROJECT_ID, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_IS_ENABLED, CASE_COUNT);
        report2Dto = new Report2Dto(PROJECT_ID, PROJECT_NAME, TOTAL_HOURS, TOTAL_INVESTED);
        projectUserHoursDto = new ProjectUserHoursDto(PROJECT_ID, PROJECT_NAME, USER_ID, TOTAL_HOURS);
        userResponse = new UserResponse(USER_ID, USER_NAME, ROLE_ID, PHOTO, USER_SALARY);
    }

    @Test
    public void getReport1Succesfully(){
        List<Report1Dto> list = new ArrayList<>();
        list.add(report1Dto);

        when(this.projectRestClient.getProjectReport()).thenReturn(list);

        List<Report1Dto> actual = this.reportService.getReport1(null);

        assertEquals(list.size(), actual.size());
        assertEquals(list.get(0), actual.get(0));

    }

    @Test
    public void getReport1SuccesfullyIsEnabled(){
        List<Report1Dto> list = new ArrayList<>();
        list.add(report1Dto);

        Report1Dto disabledProject = new Report1Dto(2, "Test", "Test", false, 2L);
        list.add(disabledProject);

        when(this.projectRestClient.getProjectReport()).thenReturn(list);

        List<Report1Dto> actual = this.reportService.getReport1(true);

        assertEquals(1, actual.size());

    }

    @Test
    public void getReport1SuccesfullyIsDisabled(){
        List<Report1Dto> list = new ArrayList<>();
        list.add(report1Dto);

        Report1Dto disabledProject = new Report1Dto(2, "Test", "Test", false, 2L);
        list.add(disabledProject);

        when(this.projectRestClient.getProjectReport()).thenReturn(list);

        List<Report1Dto> actual = this.reportService.getReport1(false);

        assertEquals(1, actual.size());
    }

    @Test
    public void getReport2Succesfully() throws UserNotFoundException, UserNotFoundException {

        when(historyRestClient.getProjectUserHoursSummary())
                .thenReturn(List.of(projectUserHoursDto));
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        List<Report2Dto> result = reportService.getReport2(null);

        assertEquals(1, result.size());
        Report2Dto report = result.get(0);

        assertEquals(PROJECT_ID, report.projectId());
        assertEquals(PROJECT_NAME, report.projectName());
        assertEquals(TOTAL_HOURS, report.totalHours());
    }

    @Test
    public void getReport2ReturnsFilteredProject() throws UserNotFoundException {
        ProjectUserHoursDto matching = new ProjectUserHoursDto(PROJECT_ID, PROJECT_NAME, USER_ID, TOTAL_HOURS);
        ProjectUserHoursDto nonMatching = new ProjectUserHoursDto(2, "test", 2, new BigDecimal(5));

        when(historyRestClient.getProjectUserHoursSummary())
                .thenReturn(List.of(matching, nonMatching));
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        List<Report2Dto> result = reportService.getReport2(PROJECT_ID);

        assertEquals(1, result.size());
        assertEquals(PROJECT_ID, result.get(0).projectId());
    }

    @Test
    public void getReport2_WhenUserNotFound_ThrowsRuntimeException() throws UserNotFoundException {

        when(historyRestClient.getProjectUserHoursSummary())
                .thenReturn(List.of(projectUserHoursDto));
        when(userRestClient.findById(USER_ID))
                .thenThrow(new UserNotFoundException("Usuario no encontrado"));

        assertThrows(RuntimeException.class, () -> reportService.getReport2(null));
    }

    @Test
    public void getReport3Succesfully() throws UserNotFoundException {
        ProjectUserHoursDto h1 = new ProjectUserHoursDto(PROJECT_ID, PROJECT_NAME, USER_ID, new BigDecimal("10"));
        ProjectUserHoursDto h2 = new ProjectUserHoursDto(PROJECT_ID, PROJECT_NAME, USER_ID, new BigDecimal("5"));
        UserResponse user = new UserResponse(USER_ID, USER_NAME, 2, PHOTO, USER_SALARY);

        when(historyRestClient.getProjectUserHoursSummary()).thenReturn(List.of(h1, h2));
        when(userRestClient.findById(USER_ID)).thenReturn(user);

        List<Report3Dto> result = reportService.getReport3(null);
        Report3Dto dto = result.get(0);

        assertEquals(1, result.size());
        assertEquals(USER_ID, dto.id());
        assertEquals(USER_NAME, dto.username());
    }

    @Test
    public void getReport3FiltersCorrectly() throws UserNotFoundException {
        ProjectUserHoursDto h1 = new ProjectUserHoursDto(PROJECT_ID, PROJECT_NAME, USER_ID, new BigDecimal("5"));
        ProjectUserHoursDto h2 = new ProjectUserHoursDto(PROJECT_ID, PROJECT_NAME, 2, new BigDecimal("8"));

        UserResponse user1 = new UserResponse(USER_ID, "TEST1", 2, PHOTO, new BigDecimal("6.00"));
        UserResponse user2 = new UserResponse(2, "TEST2", 2, PHOTO,new BigDecimal("10.00"));

        when(historyRestClient.getProjectUserHoursSummary()).thenReturn(List.of(h1, h2));
        when(userRestClient.findById(USER_ID)).thenReturn(user1);
        when(userRestClient.findById(2)).thenReturn(user2);

        List<Report3Dto> result = reportService.getReport3(USER_ID);

        assertEquals(1, result.size());
        assertEquals(USER_ID, result.get(0).id());
    }

    @Test
    public void getReport3ThrowsException() throws UserNotFoundException {

        when(historyRestClient.getProjectUserHoursSummary()).thenReturn(List.of(projectUserHoursDto));
        when(userRestClient.findById(USER_ID)).thenThrow(new UserNotFoundException("No se encontró"));

        assertThrows(UserNotFoundException.class, () -> reportService.getReport3(null));
    }

    @Test
    public void getReport4Succesfully() throws UserNotFoundException {
        CaseTypeUserHoursDto dto = new CaseTypeUserHoursDto(CASE_TYPE_ID, CASE_TYPE_NAME, USER_ID, new BigDecimal("4"));

        when(historyRestClient.getCaseTypeUserHoursReport()).thenReturn(List.of(dto));
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        List<Report4Dto> result = reportService.getReport4(null);

        assertEquals(1, result.size());

        Report4Dto report = result.get(0);
        assertEquals(CASE_TYPE_ID, report.typeId());
        assertEquals(CASE_TYPE_NAME, report.typeName());
    }

    @Test
    public void getReport4ReturnsFilteredType() throws UserNotFoundException {
        CaseTypeUserHoursDto dto1 = new CaseTypeUserHoursDto(CASE_TYPE_ID, CASE_TYPE_NAME, USER_ID, new BigDecimal("4"));
        CaseTypeUserHoursDto dto2 = new CaseTypeUserHoursDto(2, "Test", USER_ID, new BigDecimal("2"));

        when(historyRestClient.getCaseTypeUserHoursReport()).thenReturn(List.of(dto1, dto2));
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        List<Report4Dto> result = reportService.getReport4(CASE_TYPE_ID);

        assertEquals(1, result.size());
        assertEquals(CASE_TYPE_ID, result.get(0).typeId());
    }

    @Test
    public void getReport4ThrowsException() throws UserNotFoundException {
        CaseTypeUserHoursDto dto = new CaseTypeUserHoursDto(CASE_TYPE_ID, CASE_TYPE_NAME, USER_ID, new BigDecimal("4"));

        when(historyRestClient.getCaseTypeUserHoursReport()).thenReturn(List.of(dto));
        when(userRestClient.findById(USER_ID)).thenThrow(new UserNotFoundException("No se encontró"));

        assertThrows(UserNotFoundException.class, () -> reportService.getReport4(null));
    }

    @Test
    public void getReport5ReturnsAllData() throws UserNotFoundException {
        UserTimeByDateDto userTime = new UserTimeByDateDto(USER_ID, LocalDateTime.of(2025, 5, 10, 0, 0), new BigDecimal(50));

        when(historyRestClient.getUserTimeByDate()).thenReturn(List.of(userTime));
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        Report5Dto result = reportService.getReport5(null, null);

        assertEquals(new BigDecimal("50"), result.totalHours());
        assertEquals(new BigDecimal("2750"), result.totalInvested());
    }

    @Test
    public void getReport5FiltersCorrectly() throws UserNotFoundException {

        UserTimeByDateDto included = new UserTimeByDateDto(USER_ID, LocalDateTime.of(2025, 5, 12, 0, 0), new BigDecimal("3.0"));
        UserTimeByDateDto excluded = new UserTimeByDateDto(USER_ID, LocalDateTime.of(2025, 5, 5, 0, 0), new BigDecimal("2.0"));

        when(historyRestClient.getUserTimeByDate()).thenReturn(List.of(included, excluded));
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        String from = "2025-05-10T00:00:00";
        String to = "2025-05-15T00:00:00";

        Report5Dto result = reportService.getReport5(from, to);

        assertEquals(new BigDecimal("3.0"), result.totalHours());
        assertEquals(new BigDecimal("165.0"), result.totalInvested());
    }

    @Test
    public void getReport5_WhenUserNotFound_ThrowsException() throws UserNotFoundException {
        UserTimeByDateDto userTime = new UserTimeByDateDto(USER_ID, LocalDateTime.of(2025, 5, 10, 0, 0), new BigDecimal("4.0"));

        when(historyRestClient.getUserTimeByDate()).thenReturn(List.of(userTime));
        when(userRestClient.findById(USER_ID)).thenThrow(new UserNotFoundException("No encontrado"));

        assertThrows(UserNotFoundException.class, () -> reportService.getReport5(null, null));
    }

    @Test
    public void getReport6Succesfully(){

        UserResponseWithName user = new UserResponseWithName(USER_ID, USER_NAME, "email", USER_SALARY, true, "user1", "last name");
        List<UserResponseWithName> users = new ArrayList<>();
        users.add(user);

        when(this.userRestClient.findUsersByRole(2)).thenReturn(users);

        List<UserResponseWithName> act = this.reportService.getReport6();

        assertEquals(users, act);
    }

    @Test
    public void getReport7Successfully() throws UserNotFoundException {
        ProjectResponseWithoutUser project = new ProjectResponseWithoutUser(
                PROJECT_ID, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_IS_ENABLED, USER_ID
        );

        UserResponse user = new UserResponse(
                USER_ID, USER_NAME, ROLE_ID, PHOTO, USER_SALARY
        );

        when(projectRestClient.getAllProjects()).thenReturn(List.of(project));
        when(userRestClient.findById(USER_ID)).thenReturn(user);

        List<Report7Dto> result = reportService.getReport7();

        assertEquals(1, result.size());

        Report7Dto dto = result.get(0);
        assertEquals(PROJECT_ID, dto.id());
        assertEquals(PROJECT_NAME, dto.name());
        assertEquals(PROJECT_DESCRIPTION, dto.description());
        assertEquals(PROJECT_IS_ENABLED, dto.isEnabled());
        assertEquals(USER_NAME, dto.username());
    }

    @Test
    public void getReport7ThrowsException() throws UserNotFoundException {
        ProjectResponseWithoutUser project = new ProjectResponseWithoutUser(
                PROJECT_ID, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_IS_ENABLED, USER_ID
        );

        when(projectRestClient.getAllProjects()).thenReturn(List.of(project));
        when(userRestClient.findById(USER_ID)).thenThrow(new UserNotFoundException("No encontrado"));

        assertThrows(UserNotFoundException.class, () -> reportService.getReport7());
    }

    @Test
    public void getReport8Succesfully() throws UserNotFoundException {
        TopContributorDto top = new TopContributorDto(USER_ID, 12L);

        when(historyRestClient.getTopContributor()).thenReturn(top);
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        Report8Dto result = reportService.getReport8();

        assertEquals(USER_ID, result.userId());
        assertEquals(USER_NAME, result.userName());
        assertEquals(USER_SALARY, result.salaryPerHour());
        assertEquals(12L, result.cases());
    }

    @Test
    public void getReport8ThrowsException() throws UserNotFoundException {
        TopContributorDto top = new TopContributorDto(USER_ID, 12L);

        when(historyRestClient.getTopContributor()).thenReturn(top);
        when(userRestClient.findById(USER_ID)).thenThrow(new UserNotFoundException("No encontrado"));

        assertThrows(UserNotFoundException.class, () -> reportService.getReport8());
    }

    @Test
    public void getReport9Succesfully() throws UserNotFoundException {
        TopWorkerByHoursDto top = new TopWorkerByHoursDto(USER_ID, new BigDecimal("10.0"));

        when(historyRestClient.getTopWorkerByHours()).thenReturn(top);
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        Report9Dto result = reportService.getReport9();

        assertEquals(USER_ID, result.userId());
        assertEquals(USER_NAME, result.userName());
        assertEquals(USER_SALARY, result.salaryPerHour());
        assertEquals(new BigDecimal("10.0"), result.totalHours());
        assertEquals(new BigDecimal("550.0"), result.totalInvested());
    }

    @Test
    public void getReport9ThrowsException() throws UserNotFoundException {
        TopWorkerByHoursDto top = new TopWorkerByHoursDto(USER_ID, new BigDecimal("10.0"));

        when(historyRestClient.getTopWorkerByHours()).thenReturn(top);
        when(userRestClient.findById(USER_ID)).thenThrow(new UserNotFoundException("No encontrado"));

        assertThrows(UserNotFoundException.class, () -> reportService.getReport9());
    }

    @Test
    public void getReport10Succesfully() throws UserNotFoundException {
        TopProjectByCompletedCasesDto topProject = new TopProjectByCompletedCasesDto(
                PROJECT_ID, PROJECT_NAME, CASE_COUNT
        );

        when(projectRestClient.getTopProjectByCompletedCases()).thenReturn(topProject);

        TopProjectByCompletedCasesDto result = reportService.getReport10();

        assertEquals(PROJECT_ID, result.projectId());
        assertEquals(PROJECT_NAME, result.projectName());
        assertEquals(CASE_COUNT, result.totalCases());
    }

    @Test
    public void getReport11Succesfully() throws UserNotFoundException {
        TopProjectByCancelledCasesDto topProject = new TopProjectByCancelledCasesDto(
                PROJECT_ID, PROJECT_NAME, CASE_COUNT
        );

        when(projectRestClient.getTopProjectByCancelledCases()).thenReturn(topProject);

        TopProjectByCancelledCasesDto result = reportService.getReport11();

        assertEquals(PROJECT_ID, result.projectId());
        assertEquals(PROJECT_NAME, result.projectName());
        assertEquals(CASE_COUNT, result.totalCancelledCases());
    }

    @Test
    public void getReport13ReturnsAllCases() {
        CaseUserReportDto case1 = new CaseUserReportDto(
                1, "Caso 1", "Descripción 1", 1, "Tipo 1",
                LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(5), USER_ID
        );
        CaseUserReportDto case2 = new CaseUserReportDto(
                2, "Caso 2", "Descripción 2", 2, "Tipo 2",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(10), 2
        );

        when(historyRestClient.getCasesWithUserInfo()).thenReturn(List.of(case1, case2));

        List<CaseUserReportDto> result = reportService.getReport13(null);

        assertEquals(2, result.size());
    }

    @Test
    public void getReport13FiltersCorrectly() {
        CaseUserReportDto case1 = new CaseUserReportDto(
                1, "Caso 1", "Descripción 1", 1, "Tipo 1",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), USER_ID
        );
        CaseUserReportDto case2 = new CaseUserReportDto(
                2, "Caso 2", "Descripción 2", 2, "Tipo 2",
                LocalDateTime.now(), LocalDateTime.now().plusDays(2), 2
        );

        when(historyRestClient.getCasesWithUserInfo()).thenReturn(List.of(case1, case2));

        List<CaseUserReportDto> result = reportService.getReport13(USER_ID);

        assertEquals(1, result.size());
        assertEquals(USER_ID, result.get(0).userId());
    }

    @Test
    public void getReport14_WhenTypeIdIsNull_ReturnsAllCases() {
        CaseUserReportDto case1 = new CaseUserReportDto(
                1, "Caso A", "Descripción A", 1, "Tipo A",
                LocalDateTime.now(), LocalDateTime.now().plusDays(5), USER_ID
        );
        CaseUserReportDto case2 = new CaseUserReportDto(
                2, "Caso B", "Descripción B", 2, "Tipo B",
                LocalDateTime.now(), LocalDateTime.now().plusDays(10), USER_ID
        );

        when(historyRestClient.getCasesWithUserInfo()).thenReturn(List.of(case1, case2));

        List<CaseUserReportDto> result = reportService.getReport14(null);

        assertEquals(2, result.size());
        assertTrue(result.contains(case1));
        assertTrue(result.contains(case2));
    }

    @Test
    public void getReport14_WhenTypeIdProvided_FiltersCorrectly() {
        CaseUserReportDto case1 = new CaseUserReportDto(
                1, "Caso A", "Descripción A", 1, "Tipo A",
                LocalDateTime.now(), LocalDateTime.now().plusDays(5), USER_ID
        );
        CaseUserReportDto case2 = new CaseUserReportDto(
                2, "Caso B", "Descripción B", 2, "Tipo B",
                LocalDateTime.now(), LocalDateTime.now().plusDays(10), USER_ID
        );

        when(historyRestClient.getCasesWithUserInfo()).thenReturn(List.of(case1, case2));

        List<CaseUserReportDto> result = reportService.getReport14(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).caseTypeId());
    }



}
