package com.codenbugs.ms_report.dtos.report;

import java.math.BigDecimal;

public record Report5Dto (
        BigDecimal totalHours,
        BigDecimal totalInvested
){
}
