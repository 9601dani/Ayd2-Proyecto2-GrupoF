package com.codenbugs.ms_project.repositories.cases;

import com.codenbugs.ms_project.model.cases.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, Integer> {

    List<Case> findByFkProject(Integer projectId);
}
