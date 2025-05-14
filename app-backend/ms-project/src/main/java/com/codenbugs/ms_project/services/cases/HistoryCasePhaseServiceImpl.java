package com.codenbugs.ms_project.services.cases;


import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseRequest;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseResponseDto;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseWithCaseDto;
import com.codenbugs.ms_project.dtos.report.CaseTypeUserHoursDto;
import com.codenbugs.ms_project.dtos.report.ProjectUserHoursDto;
import com.codenbugs.ms_project.dtos.report.TopContributorDto;
import com.codenbugs.ms_project.dtos.report.TopWorkerByHoursDto;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.cases.CaseException;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFound;
import com.codenbugs.ms_project.exceptions.user.UserIsDisabled;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.cases.HistoryCasePhaseRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackOn = CaseException.class)
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class HistoryCasePhaseServiceImpl implements HistoryCasePhaseService {

    private final HistoryCasePhaseRepository historyCasePhaseRepository;

    @Override
    public List<HistoryCaseWithCaseDto> getAllWithCaseInfo() {
        return historyCasePhaseRepository.findAllWithCaseInfo();
    }

    @Override
    public List<ProjectUserHoursDto> getProjectUserHoursSummary() {
        return historyCasePhaseRepository.getProjectUserHoursSummary();
    }

    @Override
    public List<CaseTypeUserHoursDto> getCaseTypeUserHoursReport() {
        return historyCasePhaseRepository.getCaseTypeUserHoursReport();
    }

    @Override
    public TopContributorDto getTopContributor() {
        return historyCasePhaseRepository.getTopContributor();
    }

    @Override
    public TopWorkerByHoursDto getTopWorkerByHours() {
        return historyCasePhaseRepository.getTopWorkerByHours();
    }
}
