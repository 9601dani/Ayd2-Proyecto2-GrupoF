package com.codenbugs.ms_project.dtos.report;

import java.math.BigDecimal;

public record CaseTypeUserHoursDto(
        Integer caseTypeId,
        String caseTypeName,
        Integer userId,
        BigDecimal totalHours
) {}
