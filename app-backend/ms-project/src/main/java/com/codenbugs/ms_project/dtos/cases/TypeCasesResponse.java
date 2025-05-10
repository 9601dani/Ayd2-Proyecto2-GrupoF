package com.codenbugs.ms_project.dtos.cases;

import com.codenbugs.ms_project.model.cases.PhasesCase;
import com.codenbugs.ms_project.model.cases.TypesCase;

import java.util.List;

public record TypeCasesResponse(Integer id, String name, String description, List<PhasesCase> phases) {
    public TypeCasesResponse(TypesCase typeCase, List<PhasesCase> phases) {
        this(typeCase.getId(), typeCase.getName(), typeCase.getDescription(), phases);
    }
}


