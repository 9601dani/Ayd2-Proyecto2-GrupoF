package com.codenbugs.ms_report.dtos.report;

import java.math.BigDecimal;

public record Report2Dto(
        Integer projectId,
        String projectName,
        BigDecimal totalHours,
        BigDecimal totalInvested
) {
}
