package com.codenbugs.ms_project.repositories.typeCases;

import com.codenbugs.ms_project.model.cases.CasePhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhaseCasesRepository  extends JpaRepository<CasePhase, Integer> {

    List<CasePhase> findByFkCaseType(Integer fkCaseType);
    void deleteByFkCaseType(Integer fkCaseType);
}
