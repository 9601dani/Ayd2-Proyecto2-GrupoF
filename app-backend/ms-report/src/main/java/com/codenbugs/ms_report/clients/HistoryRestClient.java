package com.codenbugs.ms_report.clients;

import com.codenbugs.ms_report.dtos.utils.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ms-project-p", url = "http://localhost:8002/v1/histories")
public interface HistoryRestClient {

    @GetMapping("/project-user-hours")
    List<ProjectUserHoursDto> getProjectUserHoursSummary();

    @GetMapping("/case-type-user-hours")
    List<CaseTypeUserHoursDto> getCaseTypeUserHoursReport();

    @GetMapping("/top-contributor")
    TopContributorDto getTopContributor();

    @GetMapping("/top-worker")
    TopWorkerByHoursDto getTopWorkerByHours();

    @GetMapping("/cases-user-report")
    List<CaseUserReportDto> getCasesWithUserInfo();
}
