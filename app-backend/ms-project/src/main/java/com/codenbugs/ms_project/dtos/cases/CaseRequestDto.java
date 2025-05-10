package com.codenbugs.ms_project.dtos.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CaseRequestDto(
        Integer id,
        Integer fkProject,
        Integer fkCaseType,
        LocalDateTime limitDate,
        String name,
        String description
) {
}
