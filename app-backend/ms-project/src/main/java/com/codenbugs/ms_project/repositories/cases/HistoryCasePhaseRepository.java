package com.codenbugs.ms_project.repositories.cases;

import com.codenbugs.ms_project.dtos.cases.HistoryCaseWithCaseDto;
import com.codenbugs.ms_project.dtos.report.*;
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

    @Query("""
        SELECT new com.codenbugs.ms_project.dtos.report.ProjectUserHoursDto(
            c.fkProject,
            p.name,
            h.fkUser,
            SUM(h.timeSpent)
        )
        FROM HistoryCasePhase h
        JOIN Case c ON h.fkCase = c.id
        JOIN Project p ON c.fkProject = p.id
        GROUP BY c.fkProject, h.fkUser
    """)
    List<ProjectUserHoursDto> getProjectUserHoursSummary();

    @Query("""
        SELECT new com.codenbugs.ms_project.dtos.report.CaseTypeUserHoursDto(
            t.id,
            t.name,
            h.fkUser,
            SUM(h.timeSpent)
        )
        FROM HistoryCasePhase h
        JOIN Case c ON h.fkCase = c.id
        JOIN TypeCase t ON t.id = c.FK_Case_Type
        GROUP BY t.id, t.name, h.fkUser
    """)
    List<CaseTypeUserHoursDto> getCaseTypeUserHoursReport();

    @Query(value = """
        SELECT new com.codenbugs.ms_project.dtos.report.TopContributorDto(
            h.fkUser,
            COUNT(DISTINCT h.fkCase)
        )
        FROM HistoryCasePhase h
        GROUP BY h.fkUser
        ORDER BY COUNT(DISTINCT h.fkCase) DESC
        LIMIT 1
    """)
    TopContributorDto getTopContributor();

    @Query("""
        SELECT new com.codenbugs.ms_project.dtos.report.TopWorkerByHoursDto(
            h.fkUser,
            SUM(h.timeSpent)
        )
        FROM HistoryCasePhase h
        GROUP BY h.fkUser
        ORDER BY SUM(h.timeSpent) DESC
        LIMIT 1
    """)
    TopWorkerByHoursDto getTopWorkerByHours();

    Optional<HistoryCasePhase> findFirstByFkCaseOrderByIdDesc(Integer fkCase);

    List<HistoryCasePhase> findByFkUser(Integer fkUser);

    @Query("""
                SELECT new com.codenbugs.ms_project.dtos.report.CaseUserReportDto(
                    c.id,
                    c.name,
                    c.description,
                    t.id,
                    t.name,
                    c.createdAt,
                    c.limitDate,
                    h.fkUser
                )
                FROM HistoryCasePhase h
                JOIN Case c ON h.fkCase = c.id
                JOIN TypeCase t ON c.FK_Case_Type = t.id
            """)
    List<CaseUserReportDto> findAllCasesWithUserInfo();

}
