package com.codenbugs.ms_project.services.project;

import com.codenbugs.ms_project.dtos.project.ProjectRequest;
import com.codenbugs.ms_project.dtos.project.ProjectResponse;
import com.codenbugs.ms_project.exceptions.ProjectAlreadyExists;
import com.codenbugs.ms_project.model.project.Project;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectResponse saveProject(ProjectRequest request) throws ProjectAlreadyExists {

        Optional<Project> optionalProject = this.projectRepository.findByName(request.name());
        if (optionalProject.isPresent()) {
            throw new ProjectAlreadyExists("Ya existe un proyecto con ese nombre");
        }

        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setFK_User(request.fkUser());

        return null;
    }

    @Override
    public ProjectResponse updateProject(ProjectRequest request) {
        return null;
    }

    @Override
    public ProjectResponse getById(Integer id) {
        return null;
    }
}
