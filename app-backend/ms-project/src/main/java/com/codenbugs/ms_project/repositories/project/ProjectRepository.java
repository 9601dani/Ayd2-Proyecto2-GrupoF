package com.codenbugs.ms_project.repositories.project;

import com.codenbugs.ms_project.dtos.report.Report1Dto;
import com.codenbugs.ms_project.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByName(String name);

    boolean existsByIdAndIsEnabled(Integer id, Boolean isEnabled);

    @Query(value = """
                SELECT new com.codenbugs.ms_project.dtos.report.Report1Dto(
                    p.id, p.name, p.description, p.isEnabled, COUNT(c.id)
                )
                FROM Project p
                LEFT JOIN Case c ON c.fkProject = p.id
                GROUP BY p.id, p.name, p.description, p.isEnabled
                ORDER BY p.id
            """)
    List<Report1Dto> getReport1();

}
