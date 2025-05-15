package com.codenbugs.ms_project.dtos.report;

import java.math.BigDecimal;

public record TopWorkerByHoursDto(
    Integer userId,
    BigDecimal totalHours
) {}
