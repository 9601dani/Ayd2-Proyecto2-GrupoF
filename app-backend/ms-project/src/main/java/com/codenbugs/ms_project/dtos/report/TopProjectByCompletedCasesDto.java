package com.codenbugs.ms_project.dtos.report;

public record TopProjectByCompletedCasesDto(
        Integer projectId,
        String projectName,
        Long totalCases
) {}