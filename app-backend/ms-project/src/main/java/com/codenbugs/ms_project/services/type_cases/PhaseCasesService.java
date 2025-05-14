package com.codenbugs.ms_project.services.type_cases;

import com.codenbugs.ms_project.dtos.cases.PhasesCaseRequest;
import com.codenbugs.ms_project.model.cases.CasePhase;

import java.util.List;

public interface PhaseCasesService {

    List<CasePhase> findByCaseType(Integer caseId);
    CasePhase save(PhasesCaseRequest phasesCase, Integer caseId, Integer nextPhaseId);
    void deleteAllByFKCaseType(Integer caseId);

}
