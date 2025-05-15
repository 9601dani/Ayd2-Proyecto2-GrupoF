package com.codenbugs.ms_project.dtos.project;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ActiveCaseReponse(
        Integer caseId,
        String caseName,
        String description,
        BigDecimal progressPercentage,
        LocalDateTime limitDate,
        String currentPhaseName
) {
}
