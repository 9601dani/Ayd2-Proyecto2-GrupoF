package com.codenbugs.ms_project.dtos.cases;

import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.model.cases.TypeCase;

import java.util.List;

public record TypeCasesResponse(Integer id, String name, String description, List<CasePhase> phases) {
    public TypeCasesResponse(TypeCase typeCase, List<CasePhase> phases) {
        this(typeCase.getId(), typeCase.getName(), typeCase.getDescription(), phases);
    }
}


