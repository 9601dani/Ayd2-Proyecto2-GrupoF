package com.codenbugs.ms_report.dtos.report;

import java.math.BigDecimal;

public record Report3Dto(Integer id, String username, BigDecimal salaryPerHour, BigDecimal totalHours ,BigDecimal totalSalary) {
}
