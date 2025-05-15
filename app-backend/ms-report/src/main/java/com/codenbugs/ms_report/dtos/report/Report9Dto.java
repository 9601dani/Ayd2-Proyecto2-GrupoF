package com.codenbugs.ms_report.dtos.report;

import java.math.BigDecimal;

public record Report9Dto(
        Integer userId,
        String userName,
        BigDecimal salaryPerHour,
        BigDecimal totalHours,
        BigDecimal totalInvested
) {
}
