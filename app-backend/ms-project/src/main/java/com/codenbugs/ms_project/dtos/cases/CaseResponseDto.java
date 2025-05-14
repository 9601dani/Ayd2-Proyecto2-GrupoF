package com.codenbugs.ms_project.dtos.cases;

import com.codenbugs.ms_project.model.cases.Case;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CaseResponseDto(
        Integer id,
        Integer fkProject,
        BigDecimal progressPercentage,
        Integer fkCaseType,
        LocalDateTime limitDate,
        Boolean isEnabled,
        String name,
        String description,
        Boolean isCancelled,
        String reasonCancellation,
        LocalDateTime createdAt
) {

    public CaseResponseDto(Case caseEntity) {
        this(caseEntity.getId(), caseEntity.getFkProject(), caseEntity.getProgressPercentage(), caseEntity.getFK_Case_Type(), caseEntity.getLimitDate(),
                caseEntity.getIsEnabled(), caseEntity.getName(), caseEntity.getDescription(), caseEntity.getIsCancelled(), caseEntity.getReasonCancellation(), caseEntity.getCreatedAt()
        );
    }

    public CaseResponseDto(Integer caseId, Integer fkProject, BigDecimal progressPercentage, Integer fkCaseType, LocalDateTime created, Boolean enabled, String name, String description, Boolean isCancelled, String cancelReason) {
        this(caseId, fkProject, progressPercentage, fkCaseType, created, enabled, name, description, isCancelled, cancelReason, null);
    }
}
