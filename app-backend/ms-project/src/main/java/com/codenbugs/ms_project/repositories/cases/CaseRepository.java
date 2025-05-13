package com.codenbugs.ms_project.repositories.cases;

import com.codenbugs.ms_project.dtos.cases.CaseWithUserDto;
import com.codenbugs.ms_project.model.cases.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, Integer> {

    List<Case> findByFkProject(Integer projectId);

    List<Case> findByIsCancelled(Boolean isCancelled);

    @Query("""
    SELECT new com.codenbugs.ms_project.dtos.cases.CaseWithUserDto(
        c.id, c.name, c.description, c.fkProject, c.progressPercentage, c.FK_Case_Type, h.fkUser,
        c.limitDate, c.isEnabled, c.isCancelled, c.reasonCancellation
    )
    FROM Case c
    JOIN HistoryCasePhase h ON c.id = h.fkCase
    WHERE c.isEnabled = true AND c.isCancelled = false
    AND c.fkProject = :fkProject
""")
    List<CaseWithUserDto> findAllEnabledNotCancelledCasesByProject(@Param("fkProject") Integer fkProject);

}
