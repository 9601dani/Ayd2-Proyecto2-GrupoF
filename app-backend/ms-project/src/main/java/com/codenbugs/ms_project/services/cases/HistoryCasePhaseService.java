package com.codenbugs.ms_project.services.cases;

import com.codenbugs.ms_project.dtos.cases.HistoryCaseRequest;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseResponseDto;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseWithCaseDto;
import com.codenbugs.ms_project.dtos.report.CaseTypeUserHoursDto;
import com.codenbugs.ms_project.dtos.report.ProjectUserHoursDto;
import com.codenbugs.ms_project.dtos.report.TopContributorDto;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFound;
import com.codenbugs.ms_project.exceptions.user.UserIsDisabled;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;

import java.util.List;

public interface HistoryCasePhaseService {

    List<HistoryCaseWithCaseDto> getAllWithCaseInfo();

    List<ProjectUserHoursDto> getProjectUserHoursSummary();

    List<CaseTypeUserHoursDto> getCaseTypeUserHoursReport();

    TopContributorDto getTopContributor();
}
