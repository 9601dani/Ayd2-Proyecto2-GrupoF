package com.codenbugs.ms_project.dtos.cases;

import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import com.codenbugs.ms_project.model.cases.TypeCase;
import com.codenbugs.ms_project.model.project.Project;

import java.time.LocalDateTime;

public record CaseDetailsResponse(Integer id, Integer adminId, String name, String description, Integer historyId, Integer userId, Boolean isCompleted, LocalDateTime createdAt, LocalDateTime limitDate, String typeCaseName, String phaseName, Integer phaseId) {

    public CaseDetailsResponse(Case c, Project p, HistoryCasePhase historyCasePhase, TypeCase typeCase) {
        this(c.getId(), p.getFK_User(), c.getName(), c.getDescription(), historyCasePhase.getId(), historyCasePhase.getFkUser(), historyCasePhase.getIsCompleted(), c.getCreatedAt(), c.getLimitDate(), typeCase.getName(), historyCasePhase.getPhaseName(), historyCasePhase.getFkCasePhase());
    }
}
