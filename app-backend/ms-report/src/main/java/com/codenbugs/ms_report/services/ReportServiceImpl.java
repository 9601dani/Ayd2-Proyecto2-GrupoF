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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class ReportServiceImpl implements ReportService {

    private final ProjectRestClient projectRestClient;

    private final HistoryRestClient historyRestClient;

    private final UserRestClient userRestClient;

    @Override
    public List<Report1Dto> getReport1(Boolean isEnabled) {
        List<Report1Dto> report = this.projectRestClient.getProjectReport();

        if (isEnabled != null) {
            return report.stream()
                    .filter(rep -> rep.isEnabled().equals(isEnabled))
                    .toList();
        }

        return report;
    }

    @Override
    public List<Report2Dto> getReport2(Integer projectId) throws UserNotFoundException {
        List<ProjectUserHoursDto> hours = this.historyRestClient.getProjectUserHoursSummary();

        Map<Integer, List<ProjectUserHoursDto>> groupedByProject = hours.stream()
                .collect(Collectors.groupingBy(ProjectUserHoursDto::projectId));

        List<Report2Dto> reports = new ArrayList<>();

        for (Map.Entry<Integer, List<ProjectUserHoursDto>> entry : groupedByProject.entrySet()) {
            Integer currentProjectId = entry.getKey();

            if (projectId != null && !currentProjectId.equals(projectId)) {
                continue;
            }

            List<ProjectUserHoursDto> projectHours = entry.getValue();

            List<UserResponse> users = projectHours.stream()
                    .map(h -> {
                        try {
                            return this.userRestClient.findById(h.userId());
                        } catch (UserNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            BigDecimal totalHours = projectHours.stream()
                    .map(ProjectUserHoursDto::totalHours)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalInvested = BigDecimal.ZERO;
            for (int i = 0; i < users.size(); i++) {
                BigDecimal salary = users.get(i).salaryPerHour();
                BigDecimal hoursWorked = projectHours.get(i).totalHours();
                totalInvested = totalInvested.add(salary.multiply(hoursWorked));
            }

            reports.add(new Report2Dto(
                    currentProjectId,
                    projectHours.get(0).projectName(),
                    totalHours,
                    totalInvested
            ));
        }

        return reports;
    }

    @Override
    public List<Report3Dto> getReport3(Integer userId) throws UserNotFoundException {
        List<ProjectUserHoursDto> hours = this.historyRestClient.getProjectUserHoursSummary();

        List<UserResponse> users = new ArrayList<>();

        for (ProjectUserHoursDto hour : hours) {
            UserResponse user = this.userRestClient.findById(hour.userId());
            users.add(user);
        }

        BigDecimal total = new BigDecimal(0);
        BigDecimal totalHours = new BigDecimal(0);
        List<Report3Dto> reports = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            UserResponse user = users.get(i);
            ProjectUserHoursDto hour = hours.get(i);

            BigDecimal salary = user.salaryPerHour();
            BigDecimal tHours = hour.totalHours();

            Report3Dto dto = new Report3Dto(user.id(), user.username(), user.salaryPerHour(), tHours, salary.multiply(tHours));
            reports.add(dto);
        }

        if(userId != null) {
            return reports.stream().filter(r -> r.id().equals(userId)).toList();
        }
        return reports;
    }

    @Override
    public List<Report4Dto> getReport4(Integer typeId) throws UserNotFoundException {

        Map<Integer, BigDecimal> typeMapHour = new HashMap<>();
        Map<Integer, BigDecimal> typeMapInvested = new HashMap<>();
        Map<Integer, String> typeMapName = new HashMap<>();

        List<CaseTypeUserHoursDto> listTypes = this.historyRestClient.getCaseTypeUserHoursReport();

        for (CaseTypeUserHoursDto hour : listTypes) {
            UserResponse user = this.userRestClient.findById(hour.userId());

            if (typeMapHour.containsKey(hour.caseTypeId())) {
                BigDecimal currentHours = typeMapHour.get(hour.caseTypeId());
                BigDecimal updatedHours = currentHours.add(hour.totalHours());
                typeMapHour.put(hour.caseTypeId(), updatedHours);

                BigDecimal currentInvested = typeMapInvested.get(hour.caseTypeId());
                BigDecimal updatedInvested = currentInvested.add(user.salaryPerHour().multiply(hour.totalHours()));
                typeMapInvested.put(hour.caseTypeId(), updatedInvested);
            } else {
                typeMapHour.put(hour.caseTypeId(), hour.totalHours());
                typeMapInvested.put(hour.caseTypeId(), hour.totalHours().multiply(user.salaryPerHour()));
                typeMapName.put(hour.caseTypeId(), hour.caseTypeName());
            }


        }

        List<Report4Dto> reports = new ArrayList<>();

        for(Map.Entry<Integer, String> entry : typeMapName.entrySet()) {
            Report4Dto report = new Report4Dto(entry.getKey(), entry.getValue(), typeMapHour.get(entry.getKey()), typeMapInvested.get(entry.getKey()));
            reports.add(report);
        }

        if(typeId != null) {
            return reports.stream().filter(report4Dto -> report4Dto.typeId().equals(typeId)).toList();
        }

        return reports;
    }

    @Override
    public List<UserResponseWithName> getReport6() {
        List<UserResponseWithName> users = this.userRestClient.findUsersByRole(2);
        return users;
    }

    @Override
    public List<ProjectResponseWithoutUser> getReport7() {
        return this.projectRestClient.getAllProjects();
    }

    @Override
    public Report8Dto getReport8() throws UserNotFoundException {

        TopContributorDto top = this.historyRestClient.getTopContributor();

        UserResponse user = this.userRestClient.findById(top.userId());

        return new Report8Dto(user.id(), user.username(), user.salaryPerHour(), top.totalCases());
    }

    @Override
    public Report9Dto getReport9() throws UserNotFoundException {

        TopWorkerByHoursDto top = this.historyRestClient.getTopWorkerByHours();

        UserResponse user = this.userRestClient.findById(top.userId());

        return new Report9Dto(user.id(), user.username(), user.salaryPerHour(), top.totalHours(), top.totalHours().multiply(user.salaryPerHour()));
    }

    @Override
    public TopProjectByCompletedCasesDto getReport10() throws UserNotFoundException {
        return this.projectRestClient.getTopProjectByCompletedCases();
    }
}
