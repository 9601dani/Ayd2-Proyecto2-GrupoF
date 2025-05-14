package com.codenbugs.ms_project.dtos.report;

import java.math.BigDecimal;

public record ProjectUserHoursDto(
        Integer projectId,
        String projectName,
        Integer userId,
        BigDecimal totalHours
) {}

