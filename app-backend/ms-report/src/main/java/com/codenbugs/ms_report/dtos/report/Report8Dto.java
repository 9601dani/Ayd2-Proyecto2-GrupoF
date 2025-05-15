package com.codenbugs.ms_report.dtos.report;

import java.math.BigDecimal;

public record Report8Dto(
        Integer userId,
        String userName,
        BigDecimal salaryPerHour,
        Long cases
) {
}
