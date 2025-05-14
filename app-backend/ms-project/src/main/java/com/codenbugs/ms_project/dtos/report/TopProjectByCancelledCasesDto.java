package com.codenbugs.ms_project.dtos.report;

public record TopProjectByCancelledCasesDto(
    Integer projectId,
    String projectName,
    Long totalCancelledCases
) {}