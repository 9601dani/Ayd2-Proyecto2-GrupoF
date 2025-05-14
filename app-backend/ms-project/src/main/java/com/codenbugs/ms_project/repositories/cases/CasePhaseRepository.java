package com.codenbugs.ms_project.repositories.cases;

import com.codenbugs.ms_project.model.cases.CasePhase;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CasePhaseRepository extends JpaRepository<CasePhase, Integer> {

    @Query(value = """
        SELECT ROUND(100.0 / COUNT(*), 2) AS porcentajePorFase
        FROM case_phases
        WHERE FK_Case_Type = :caseType
        """, nativeQuery = true)
    Double getPercentageByFkCaseType(@Param("caseType") Integer caseType);
}
