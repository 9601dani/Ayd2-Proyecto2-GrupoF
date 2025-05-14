package com.codenbugs.ms_report.dtos.utils;

import java.math.BigDecimal;

public record CaseTypeUserHoursDto(
        Integer caseTypeId,
        String caseTypeName,
        Integer userId,
        BigDecimal totalHours
) {}
