package com.codenbugs.ms_project.dtos.cases;

import java.math.BigDecimal;

public record HistoryCaseRequest (
        Integer id,
        Integer fkCase,
        Integer fkUser,
        Integer fkCasePhase,
        BigDecimal timeSpent
){
}
