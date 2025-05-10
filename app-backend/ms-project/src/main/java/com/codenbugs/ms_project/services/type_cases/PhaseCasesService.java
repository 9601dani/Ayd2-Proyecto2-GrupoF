package com.codenbugs.ms_project.services.type_cases;

import com.codenbugs.ms_project.dtos.cases.PhasesCaseRequest;
import com.codenbugs.ms_project.model.cases.PhasesCase;

import java.util.List;

public interface PhaseCasesService {

    List<PhasesCase> findByCaseType(Integer caseId);
    PhasesCase save(PhasesCaseRequest phasesCase, Integer caseId, Integer nextPhaseId);

}
