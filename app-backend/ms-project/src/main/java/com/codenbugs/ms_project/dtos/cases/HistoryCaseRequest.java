package com.codenbugs.ms_project.dtos.cases;

import java.math.BigDecimal;

public record HistoryCaseRequest (
        Integer id,
        Boolean isCompleted,
        BigDecimal timeSpent
){
}
