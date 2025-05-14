package com.codenbugs.ms_report.dtos.utils;

import java.math.BigDecimal;

public record TopWorkerByHoursDto(
    Integer userId,
    BigDecimal totalHours
) {}
