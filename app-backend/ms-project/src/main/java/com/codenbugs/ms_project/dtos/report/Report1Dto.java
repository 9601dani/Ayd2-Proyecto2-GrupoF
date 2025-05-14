package com.codenbugs.ms_project.dtos.report;

public record Report1Dto(
        Integer projectId,
        String projectName,
        String description,
        Boolean isEnabled,
        Long caseCount
) {}
