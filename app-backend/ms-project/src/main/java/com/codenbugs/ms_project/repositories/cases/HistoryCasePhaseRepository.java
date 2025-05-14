package com.codenbugs.ms_project.repositories.cases;

import com.codenbugs.ms_project.dtos.cases.HistoryCaseWithCaseDto;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryCasePhaseRepository extends JpaRepository<HistoryCasePhase, Integer> {

    void deleteAllHistoryCasePhaseByFkCase(Integer fkCase);

    @Query("""
        SELECT new com.codenbugs.ms_project.dtos.cases.HistoryCaseWithCaseDto(
            h.id, h.fkCase, h.fkUser, h.fkCasePhase, h.isCompleted, h.timeSpent, h.phaseName,
            c.fkProject, c.progressPercentage, c.limitDate, c.isEnabled, c.isCancelled, c.createdAt
        )
        FROM HistoryCasePhase h
        JOIN Case c ON h.fkCase = c.id
    """)
    List<HistoryCaseWithCaseDto> findAllWithCaseInfo();

    Optional<HistoryCasePhase> findFirstByFkCaseOrderByIdDesc(Integer fkCase);
    List<HistoryCasePhase> findByFkUser(Integer fkUser);
}
