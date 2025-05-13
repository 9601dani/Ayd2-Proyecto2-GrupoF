package com.codenbugs.ms_project.dtos.cases;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoryCaseWithCaseDto(
        Integer id,
        Integer fkCase,
        Integer fkUser,
        Integer fkCasePhase,
        Boolean isCompleted,
        BigDecimal timeSpent,
        String phaseName,
        Integer fkProject,
        BigDecimal progressPercentage,
        LocalDateTime limitDate,
        Boolean isEnabled,
        Boolean isCancelled,
        LocalDateTime createdAt
) {}

