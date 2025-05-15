package com.codenbugs.ms_project.services.cases;


import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.cases.*;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseRequest;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseResponseDto;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseWithCaseDto;
import com.codenbugs.ms_project.dtos.report.*;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.cases.CaseException;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.cases.CasePhaseNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import com.codenbugs.ms_project.model.cases.TypeCase;
import com.codenbugs.ms_project.model.project.Project;
import com.codenbugs.ms_project.repositories.cases.CasePhaseRepository;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.cases.HistoryCasePhaseRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import com.codenbugs.ms_project.repositories.typeCases.TypeCasesRepository;
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
@Transactional(rollbackOn = Exception.class)
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class HistoryCasePhaseServiceImpl implements HistoryCasePhaseService {

    private final HistoryCasePhaseRepository historyCasePhaseRepository;
    private final CasePhaseRepository casePhaseRepository;
    private final TypeCasesRepository typeCasesRepository;
    private final CaseRepository caseRepository;
    private final UserRestClient userRestClient;
    private final ProjectRepository projectRepository;

    @Override
    public List<HistoryCaseWithCaseDto> getAllWithCaseInfo() {
        return historyCasePhaseRepository.findAllWithCaseInfo();
    }

    @Override
    public CasePhaseResponse getNextPhase(Integer id) throws CasePhaseNotFoundException {
        CasePhase currentPhase = this.casePhaseRepository.findById(id)
                .orElseThrow(() -> new CasePhaseNotFoundException("No se encontró la fase actual"));

        if(currentPhase.getNextPhase() == null) return null;

        CasePhase nextPhase = this.casePhaseRepository.findById(currentPhase.getNextPhase())
                .orElseThrow(() -> new CasePhaseNotFoundException("No se encontró la siguiente fase"));

        TypeCase typeCase = this.typeCasesRepository.findById(nextPhase.getFkCaseType())
                .orElseThrow(() -> new CasePhaseNotFoundException("No se encontró la fase"));

        return new CasePhaseResponse(nextPhase, typeCase);
    }

    @Override
    public HistoryCaseResponseDto updateCasePhase(HistoryCaseRequest request) throws CasePhaseNotFoundException, CaseNotFoundException {
        HistoryCasePhase historyCasePhase = this.historyCasePhaseRepository.findById(request.id())
                .orElseThrow(() -> new CasePhaseNotFoundException("No se encontró la fase"));

        Case c = this.caseRepository.findById(historyCasePhase.getFkCase())
                .orElseThrow(() -> new CasePhaseNotFoundException("No se encontró el caso."));

        Project p = this.projectRepository.findByIdAndIsEnabled(c.getFkProject(), true)
                .orElseThrow(() -> new CaseNotFoundException("El caso no existe"));

        if(!c.getIsEnabled()) {
            throw new CasePhaseNotFoundException("El caso no se encuentra habilitado.");
        }

        if(!p.getIsEnabled()) {
            throw new CaseNotFoundException("El proyecto no se encuentra habilitado.");
        }

        historyCasePhase.setIsCompleted(request.isCompleted());

        if(request.timeSpent() != null) {
            historyCasePhase.setTimeSpent(historyCasePhase.getTimeSpent().add(request.timeSpent()));
        }

        historyCasePhase = this.historyCasePhaseRepository.save(historyCasePhase);
        return new HistoryCaseResponseDto(historyCasePhase);
    }

    @Override
    public HistoryCaseResponseDto saveNextPhase(NextPhaseRequest request) throws CasePhaseNotFoundException, CaseNotFoundException, UserNotFoundException {
        Case c = this.caseRepository.findById(request.caseId())
                .orElseThrow(() -> new CaseNotFoundException("No se encontró el caso."));

        Project p = this.projectRepository.findByIdAndIsEnabled(c.getFkProject(), true)
                .orElseThrow(() -> new CaseNotFoundException("El caso no existe"));

        if(!c.getIsEnabled()) {
            throw new CaseNotFoundException("El caso no se encuentra habilitado.");
        }

        if(!p.getIsEnabled()) {
            throw new CaseNotFoundException("El proyecto no se encuentra habilitado.");
        }

        CasePhase casePhase = this.casePhaseRepository.findById(request.nextPhaseId())
                .orElseThrow(() -> new CasePhaseNotFoundException("No se encontró la siguiente fase"));

        UserResponse userResponse = this.userRestClient.findById(request.userId());

        if(!userResponse.isEnabled()) {
            throw new CaseNotFoundException("El usuario no se encuentra habilitado.");
        }

        HistoryCasePhase historyCasePhase = new HistoryCasePhase();
        historyCasePhase.setFkCase(c.getId());
        historyCasePhase.setFkUser(userResponse.id());
        historyCasePhase.setFkCasePhase(casePhase.getId());
        historyCasePhase.setTimeSpent(BigDecimal.ZERO);
        historyCasePhase.setIsCompleted(false);
        historyCasePhase.setPhaseName(casePhase.getName());

        historyCasePhase = this.historyCasePhaseRepository.save(historyCasePhase);

        this.updatePercentage(c);

        return new HistoryCaseResponseDto(historyCasePhase);
    }

    @Override
    public void completeCase(Integer id) throws CaseNotFoundException {
        Case c = this.caseRepository.findById(id)
                .orElseThrow(() -> new CaseNotFoundException("No se encontró el caso."));

        Project p = this.projectRepository.findByIdAndIsEnabled(c.getFkProject(), true)
                .orElseThrow(() -> new CaseNotFoundException("El caso no existe"));

        if(!c.getIsEnabled()) {
            throw new CaseNotFoundException("El caso no se encuentra habilitado.");
        }

        if(!p.getIsEnabled()) {
            throw new CaseNotFoundException("El proyecto no se encuentra habilitado.");
        }

        this.updatePercentage(c);
    }

    public void updatePercentage(Case c) {
        Double percentage = this.casePhaseRepository.getPercentageByFkCaseType(c.getFK_Case_Type());
        if (c.getProgressPercentage().doubleValue() < 100) {
            var value = c.getProgressPercentage().add(BigDecimal.valueOf(percentage));
            c.setProgressPercentage(value.doubleValue() > 100 ? BigDecimal.valueOf(100) : value);
        }
    }

    @Override
    public List<HistoryCasePhase> findByFkUser(Integer fkUser) {
        return historyCasePhaseRepository.findByFkUser(fkUser);
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

    @Override
    public List<CaseUserReportDto> getCasesWithUserInfo() {
        return historyCasePhaseRepository.findAllCasesWithUserInfo();
    }

    @Override
    public List<UserTimeByDateDto> getUserTimeByDate() {
        return historyCasePhaseRepository.getUserTimeGroupedByDate();
    }



}
