package com.codenbugs.ms_report.services;

import com.codenbugs.ms_report.clients.HistoryRestClient;
import com.codenbugs.ms_report.clients.ProjectRestClient;
import com.codenbugs.ms_report.clients.UserRestClient;
import com.codenbugs.ms_report.dtos.report.Report1Dto;
import com.codenbugs.ms_report.dtos.report.Report2Dto;
import com.codenbugs.ms_report.dtos.user.UserResponse;
import com.codenbugs.ms_report.dtos.utils.ProjectUserHoursDto;
import com.codenbugs.ms_report.exceptions.user.UserNotFoundException;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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
    public void getReport2_WhenProjectIdNull_ReturnsAllProjectsSummary() throws UserNotFoundException, UserNotFoundException {

        when(historyRestClient.getProjectUserHoursSummary())
                .thenReturn(List.of(projectUserHoursDto));
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        List<Report2Dto> result = reportService.getReport2(null);

        assertEquals(1, result.size());
        Report2Dto report = result.get(0);

        assertEquals(PROJECT_ID, report.projectId());
        assertEquals(PROJECT_NAME, report.projectName());
        assertEquals(TOTAL_HOURS, report.totalHours()); // 4.19 * 24
    }

    @Test
    public void getReport2_WhenSpecificProjectId_ReturnsFilteredProject() throws UserNotFoundException {
        ProjectUserHoursDto matching = new ProjectUserHoursDto(PROJECT_ID, PROJECT_NAME, USER_ID, TOTAL_HOURS);
        ProjectUserHoursDto nonMatching = new ProjectUserHoursDto(2, "test", 2, new BigDecimal(5));

        when(historyRestClient.getProjectUserHoursSummary())
                .thenReturn(List.of(matching, nonMatching));
        when(userRestClient.findById(USER_ID)).thenReturn(userResponse);

        List<Report2Dto> result = reportService.getReport2(PROJECT_ID);

        assertEquals(1, result.size());
        assertEquals(PROJECT_ID, result.get(0).projectId());
    }


}
