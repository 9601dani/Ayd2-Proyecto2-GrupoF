package com.codenbugs.ms_report.dtos.utils;

public record TopProjectByCompletedCasesDto(
        Integer projectId,
        String projectName,
        Long totalCases
) {}