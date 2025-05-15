package com.codenbugs.ms_project.dtos.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserTimeByDateDto(
    Integer userId,
    LocalDateTime createdAt,
    BigDecimal totalHours
) {}
