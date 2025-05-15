package com.codenbugs.ms_report.dtos.report;

import java.math.BigDecimal;

public record Report4Dto(
        Integer typeId,
        String typeName,
        BigDecimal totalHours,
        BigDecimal totalInvested
) {
}
