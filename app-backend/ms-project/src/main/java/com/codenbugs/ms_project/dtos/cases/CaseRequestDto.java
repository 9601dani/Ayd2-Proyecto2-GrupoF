package com.codenbugs.ms_project.dtos.cases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CaseRequestDto(
        Integer id,
        Integer fkProject,
        Integer fkCaseType,
        Integer fkUser,
        LocalDateTime limitDate,
        String name,
        String description
) {
}
