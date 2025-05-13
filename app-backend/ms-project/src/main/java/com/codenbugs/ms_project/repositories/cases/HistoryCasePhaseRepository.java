package com.codenbugs.ms_project.repositories.cases;

import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryCasePhaseRepository extends JpaRepository<HistoryCasePhase, Integer> {

    void deleteAllHistoryCasePhaseByFkCase(Integer fkCase);
}
