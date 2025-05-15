package com.codenbugs.ms_project.services.cases;


import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.cases.*;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.cases.CaseAlreadyExistException;
import com.codenbugs.ms_project.exceptions.cases.CaseException;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserIsDisabled;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.model.cases.TypeCase;
import com.codenbugs.ms_project.model.project.Project;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.cases.HistoryCasePhaseRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import com.codenbugs.ms_project.repositories.typeCases.PhaseCasesRepository;
import com.codenbugs.ms_project.repositories.typeCases.TypeCasesRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = CaseException.class)
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class CaseServiceImpl implements CaseService{

    private final CaseRepository caseRepository;
    private final HistoryCasePhaseRepository historyCasePhaseRepository;
    private final PhaseCasesRepository phaseCasesRepository;
    private final ProjectRepository projectRepository;
    private final UserRestClient userRestClient;
    private final TypeCasesRepository typeCasesRepository;

    @Override
    public CaseResponseDto saveCase(CaseRequestDto request) throws ProjectNotFoundException, ProjectIsDisabled, UserNotFoundException, UserIsDisabled, CaseException {

        Optional<Project> optionalProject = this.projectRepository.findById(request.fkProject());

        if(optionalProject.isEmpty()) {
            throw new ProjectNotFoundException("No se encontro el proyecto");
        }

        Project project = optionalProject.get();

        if(!project.getIsEnabled()){
            throw new ProjectIsDisabled("El proyecto esta deshabilitado");
        }

        UserResponse user = this.userRestClient.findById(request.fkUser());

        if(!user.isEnabled()){
            throw new UserIsDisabled("El usuario esta deshabilitado");
        }

        if (!caseRepository.findByNameAndFkProject(request.name(), request.fkProject()).isEmpty()) {
            throw new CaseAlreadyExistException("Ya existe un caso con ese nombre en este proyecto");
        }

        Case newCase = new Case();
        newCase.setName(request.name());
        newCase.setDescription(request.description());
        newCase.setFkProject(project.getId());
        newCase.setFK_Case_Type(request.fkCaseType());
        newCase.setProgressPercentage(BigDecimal.valueOf(0));
        newCase.setLimitDate(request.limitDate());
        newCase.setIsCancelled(false);
        newCase.setIsEnabled(true);
        newCase.setCreatedAt(request.createdAt());

        Case savedCase = this.caseRepository.save(newCase);

        List<CasePhase> phases = this.phaseCasesRepository.findByFkCaseType(request.fkCaseType());

        Collections.reverse(phases);

        CasePhase firstPhase = phases.get(0);

        HistoryCasePhase hcp = new HistoryCasePhase();
        hcp.setFkCase(savedCase.getId());
        hcp.setFkUser(user.id());
        hcp.setFkCasePhase(firstPhase.getId());
        hcp.setIsCompleted(false);
        hcp.setTimeSpent(BigDecimal.ZERO);
        hcp.setPhaseName(firstPhase.getName());

        this.historyCasePhaseRepository.save(hcp);

        return new CaseResponseDto(savedCase);
    }

    @Override
    public CaseResponseDto getCaseById(Integer id) throws CaseNotFoundException {

        Optional<Case> optionalCase = this.caseRepository.findById(id);

        if(optionalCase.isEmpty()) {
            throw new CaseNotFoundException("El caso no existe");
        }
        return new CaseResponseDto(optionalCase.get());
    }

    @Override
    public CaseResponseDto updateCase(CaseRequestDto request) throws CaseIsDisabled, CaseException {

        Optional<Case> optionalCase = this.caseRepository.findById(request.id());

        if(optionalCase.isEmpty()) {
            throw new CaseNotFoundException("El caso no existe");
        }

        Case caseToUpdate = optionalCase.get();

        if(!caseToUpdate.getIsEnabled()){
            throw new CaseIsDisabled("El caso está deshabilitado");
        }

        if(caseToUpdate.getIsCancelled()){
            throw new CaseIsDisabled("El caso está cancelado");
        }

        if (!caseToUpdate.getName().equalsIgnoreCase(request.name())) {
            List<Case> existingCases = caseRepository.findByNameAndFkProject(request.name(), request.fkProject());

            boolean nameConflict = existingCases.stream()
                    .anyMatch(c -> !c.getId().equals(request.id()));

            if (nameConflict) {
                throw new CaseAlreadyExistException("Ya existe un caso con ese nombre en este proyecto");
            }
        }

        caseToUpdate.setName(request.name());
        caseToUpdate.setDescription(request.description());
        caseToUpdate.setLimitDate(request.limitDate());

        if(!caseToUpdate.getFK_Case_Type().equals(request.fkCaseType())){

            caseToUpdate.setFK_Case_Type(request.fkCaseType());
            List<CasePhase> phases = this.phaseCasesRepository.findByFkCaseType(request.fkCaseType());

            Collections.reverse(phases);

            CasePhase firstPhase = phases.get(0);

            this.historyCasePhaseRepository.deleteAllHistoryCasePhaseByFkCase(caseToUpdate.getId());

            HistoryCasePhase hcp = new HistoryCasePhase();
            hcp.setFkCase(caseToUpdate.getId());
            hcp.setFkUser(request.fkUser());
            hcp.setFkCasePhase(firstPhase.getId());
            hcp.setIsCompleted(false);
            hcp.setTimeSpent(BigDecimal.ZERO);
            hcp.setPhaseName(firstPhase.getName());

            this.historyCasePhaseRepository.save(hcp);

            caseToUpdate.setProgressPercentage(BigDecimal.ZERO);
        }

        Case updatedCase = this.caseRepository.save(caseToUpdate);

        return new CaseResponseDto(updatedCase);
    }

    @Override
    public CaseResponseDto cancelCase(CaseCancelledRequestDto request) throws CaseNotFoundException, CaseIsDisabled {

        Optional<Case> optionalCase = this.caseRepository.findById(request.id());

        if(optionalCase.isEmpty()) {
            throw new CaseNotFoundException("El caso no existe");
        }

        Case caseToCancel = optionalCase.get();

        if(caseToCancel.getIsCancelled()){
            throw new CaseIsDisabled("El caso ya está cancelado");
        }

        caseToCancel.setIsCancelled(true);
        caseToCancel.setReasonCancellation(request.reasonCancellation());

        Case updatedCase = this.caseRepository.save(caseToCancel);

        return new CaseResponseDto(updatedCase);
    }

    @Override
    public List<CaseResponseDto> getCasesByProjectId(Integer projectId) {
        return this.caseRepository.findByFkProject(projectId).stream().map(CaseResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CaseWithUserDto> getActiveCasesByProject(Integer fkProject) {
        return caseRepository.findAllEnabledNotCancelledCasesByProject(fkProject);
    }

    @Override
    public List<CaseResponseDto> getCasesByIsCancelled(Boolean isCancelled) {
        return this.caseRepository.findByIsCancelled(isCancelled).stream().map(CaseResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public CaseDetailsResponse getCaseDetails(Integer id) throws CaseNotFoundException {
        Case c = this.caseRepository.findById(id).orElseThrow(() -> new CaseNotFoundException("El caso no existe"));
        Project p = this.projectRepository.findByIdAndIsEnabled(c.getFkProject(), true)
                .orElseThrow(() -> new CaseNotFoundException("El caso no existe"));

        if(!c.getIsEnabled()) {
            throw new CaseNotFoundException("El caso no se encuentra activo.");
        }

        if(!p.getIsEnabled()) {
            throw new CaseNotFoundException("El caso no existe");
        }

        HistoryCasePhase historyCasePhase = this.historyCasePhaseRepository.findFirstByFkCaseOrderByIdDesc(c.getId())
                .orElseThrow(() -> new CaseNotFoundException("El caso no existe"));

        TypeCase typeCase = this.typeCasesRepository.findById(c.getFK_Case_Type())
                .orElseThrow(() -> new CaseNotFoundException("El caso no existe"));

        return new CaseDetailsResponse(c, p, historyCasePhase, typeCase);
    }
}
