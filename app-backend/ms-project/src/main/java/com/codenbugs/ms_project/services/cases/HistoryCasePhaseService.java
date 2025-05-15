package com.codenbugs.ms_project.services.cases;

import com.codenbugs.ms_project.dtos.cases.*;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.cases.CasePhaseNotFoundException;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseRequest;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseResponseDto;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseWithCaseDto;
import com.codenbugs.ms_project.dtos.report.CaseTypeUserHoursDto;
import com.codenbugs.ms_project.dtos.report.ProjectUserHoursDto;
import com.codenbugs.ms_project.dtos.report.TopContributorDto;
import com.codenbugs.ms_project.dtos.report.TopWorkerByHoursDto;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;

import java.util.List;

import java.util.List;

public interface HistoryCasePhaseService {

    List<HistoryCaseWithCaseDto> getAllWithCaseInfo();

    CasePhaseResponse getNextPhase(Integer id) throws CasePhaseNotFoundException;

    HistoryCaseResponseDto updateCasePhase(HistoryCaseRequest request) throws CasePhaseNotFoundException, CaseNotFoundException;

    HistoryCaseResponseDto saveNextPhase(NextPhaseRequest request) throws CasePhaseNotFoundException, CaseNotFoundException, UserNotFoundException;

    List<HistoryCasePhase> findByFkUser(Integer fkUser);

    void completeCase(Integer id) throws CaseNotFoundException;

    List<ProjectUserHoursDto> getProjectUserHoursSummary();

    List<CaseTypeUserHoursDto> getCaseTypeUserHoursReport();

    TopContributorDto getTopContributor();

    TopWorkerByHoursDto getTopWorkerByHours();
}
