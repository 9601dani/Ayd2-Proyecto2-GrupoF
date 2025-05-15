package com.codenbugs.ms_report.dtos.utils;

import java.math.BigDecimal;

public record ProjectUserHoursDto(
        Integer projectId,
        String projectName,
        Integer userId,
        BigDecimal totalHours
) {}

