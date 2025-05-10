package com.codenbugs.ms_project.services.cases;


import com.codenbugs.ms_project.dtos.cases.CaseCancelledRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseResponseDto;
import com.codenbugs.ms_project.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_project.exceptions.cases.CaseException;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFound;
import com.codenbugs.ms_project.exceptions.project.ProjectException;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFound;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.project.Project;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final ProjectRepository projectRepository;

    @Override
    public CaseResponseDto saveCase(CaseRequestDto request) throws ProjectNotFound, ProjectIsDisabled {

        Optional<Project> optionalProject = this.projectRepository.findById(request.fkProject());

        if(optionalProject.isEmpty()) {
            throw new ProjectNotFound("No se encontro el proyecto");
        }

        Project project = optionalProject.get();

        if(!project.getIsEnabled()){
            throw new ProjectIsDisabled("El proyecto esta deshabilitado");
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

        Case savedCase = this.caseRepository.save(newCase);

        return new CaseResponseDto(savedCase);
    }

    @Override
    public CaseResponseDto getCaseById(Integer id) throws CaseNotFound {

        Optional<Case> optionalCase = this.caseRepository.findById(id);

        if(optionalCase.isEmpty()) {
            throw new CaseNotFound("El caso no existe");
        }
        return new CaseResponseDto(optionalCase.get());
    }

    @Override
    public CaseResponseDto updateCase(CaseRequestDto request) throws CaseIsDisabled, CaseNotFound {

        Optional<Case> optionalCase = this.caseRepository.findById(request.id());

        if(optionalCase.isEmpty()) {
            throw new CaseNotFound("El caso no existe");
        }

        Case caseToUpdate = optionalCase.get();

        if(!caseToUpdate.getIsEnabled()){
            throw new CaseIsDisabled("El caso está deshabilitado");
        }

        if(caseToUpdate.getIsCancelled()){
            throw new CaseIsDisabled("El caso está cancelado");
        }

        caseToUpdate.setName(request.name());
        caseToUpdate.setDescription(request.description());
        caseToUpdate.setFK_Case_Type(request.fkCaseType());
        caseToUpdate.setLimitDate(request.limitDate());

        Case updatedCase = this.caseRepository.save(caseToUpdate);

        return new CaseResponseDto(updatedCase);
    }

    @Override
    public CaseResponseDto cancelCase(CaseCancelledRequestDto request) throws CaseNotFound, CaseIsDisabled {

        Optional<Case> optionalCase = this.caseRepository.findById(request.id());

        if(optionalCase.isEmpty()) {
            throw new CaseNotFound("El caso no existe");
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
}
