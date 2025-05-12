package com.codenbugs.ms_project.dtos.cases;

import com.codenbugs.ms_project.model.cases.HistoryCasePhase;

import java.math.BigDecimal;

public record HistoryCaseResponseDto (
        Integer id,
        Integer fkCase,
        Integer fkUser,
        Integer fkCasePhase,
        Boolean isCompleted,
        BigDecimal timeSpent
){
    public HistoryCaseResponseDto(HistoryCasePhase historyCasePhase) {
        this(historyCasePhase.getId(), historyCasePhase.getFkCase(), historyCasePhase.getFkUser(), historyCasePhase.getFkCasePhase(), historyCasePhase.getIsCompleted(), historyCasePhase.getTimeSpent());
    }
}
