package com.codenbugs.ms_report.clients;

import com.codenbugs.ms_report.dtos.utils.CaseTypeUserHoursDto;
import com.codenbugs.ms_report.dtos.utils.ProjectUserHoursDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ms-project-p", url = "http://localhost:8002/v1/history")
public interface HistoryRestClient {

    @GetMapping("/project-user-hours")
    List<ProjectUserHoursDto> getProjectUserHoursSummary();

    @GetMapping("/case-type-user-hours")
    List<CaseTypeUserHoursDto> getCaseTypeUserHoursReport();
}
