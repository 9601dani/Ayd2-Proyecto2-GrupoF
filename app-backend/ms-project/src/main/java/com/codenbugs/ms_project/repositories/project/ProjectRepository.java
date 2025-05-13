package com.codenbugs.ms_project.repositories.project;

import com.codenbugs.ms_project.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByName(String name);

    boolean existsByIdAndIsEnabled(Integer id, Boolean isEnabled);
}
