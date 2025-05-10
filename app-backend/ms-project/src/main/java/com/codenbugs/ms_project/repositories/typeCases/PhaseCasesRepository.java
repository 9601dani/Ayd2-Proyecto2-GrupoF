package com.codenbugs.ms_project.repositories.typeCases;

import com.codenbugs.ms_project.model.cases.PhasesCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhaseCasesRepository  extends JpaRepository<PhasesCase, Integer> {

    List<PhasesCase> findByFkCaseType(Integer fkCaseType);
}
