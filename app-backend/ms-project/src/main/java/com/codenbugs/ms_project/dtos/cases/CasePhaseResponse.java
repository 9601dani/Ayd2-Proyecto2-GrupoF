package com.codenbugs.ms_project.dtos.cases;

import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.model.cases.TypeCase;

public record CasePhaseResponse(Integer id, String name, String typeCaseName) {

    public CasePhaseResponse(CasePhase casePhase, TypeCase typeCase) {
        this(casePhase.getId(), casePhase.getName(), typeCase.getName());
    }
}
