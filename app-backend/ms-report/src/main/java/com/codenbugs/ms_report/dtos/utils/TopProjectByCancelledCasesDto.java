package com.codenbugs.ms_report.dtos.utils;

public record TopProjectByCancelledCasesDto(
    Integer projectId,
    String projectName,
    Long totalCancelledCases
) {}