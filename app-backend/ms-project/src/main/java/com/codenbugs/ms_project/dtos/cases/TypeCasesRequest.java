package com.codenbugs.ms_project.dtos.cases;

import java.util.List;

public record TypeCasesRequest(
        String name,
        String description,
        List<PhasesCaseRequest> phases
) {}

