package com.codenbugs.ms_report.dtos.utils;

import java.time.LocalDateTime;

public record CaseUserReportDto(
    Integer caseId,
    String caseName,
    String description,
    Integer caseTypeId,
    String caseTypeName,
    LocalDateTime createdAt,
    LocalDateTime limitDate,
    Integer userId
) {}
