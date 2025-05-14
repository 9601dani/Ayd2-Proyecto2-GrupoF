package com.codenbugs.ms_project.services.project;

import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.project.ProjectEnabledRequest;
import com.codenbugs.ms_project.dtos.project.ProjectRequest;
import com.codenbugs.ms_project.dtos.project.ProjectResponse;
import com.codenbugs.ms_project.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.project.ProjectAlreadyExists;
import com.codenbugs.ms_project.exceptions.project.ProjectException;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.project.Project;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = ProjectException.class)
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRestClient userRestClient;
    private final CaseRepository caseRepository;

    @Override
    public ProjectResponseWithoutUser saveProject(ProjectRequest request) throws ProjectAlreadyExists {

        Optional<Project> optionalProject = this.projectRepository.findByName(request.name());
        if (optionalProject.isPresent()) {
            throw new ProjectAlreadyExists("Ya existe un proyecto con ese nombre");
        }

        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setFK_User(request.fkUser());
        project.setIsEnabled(true);

        Project saved = this.projectRepository.save(project);

        return new ProjectResponseWithoutUser(saved);
    }

    @Override
    public ProjectResponseWithoutUser updateProject(ProjectRequest request) throws ProjectNotFoundException, ProjectIsDisabled {

        Optional<Project> optionalProject = this.projectRepository.findById(request.id());
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException("El proyecto no existe");
        }

        Project project = optionalProject.get();

        if (!project.getIsEnabled()) {
            throw new ProjectIsDisabled("El proyecto esta deshabilidato");
        }

        project.setName(request.name());
        project.setDescription(request.description());
        project.setFK_User(request.fkUser());

        Project updated = this.projectRepository.save(project);

        return new ProjectResponseWithoutUser(updated);
    }

    @Override
    public ProjectResponse getById(Integer id) throws ProjectNotFoundException, UserNotFoundException {
        Optional<Project> optionalProject = this.projectRepository.findById(id);
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException("El proyecto no existe");
        }

        Project project = optionalProject.get();
        UserResponse user = userRestClient.findById(project.getFK_User());

        return new ProjectResponse(project, user);
    }

    @Override
    public List<ProjectResponseWithoutUser> getAllProjects() {
        return this.projectRepository.findAll().stream().map(ProjectResponseWithoutUser::new).collect(Collectors.toList());
    }

    @Override
    public ProjectResponseWithoutUser updateEnabled(ProjectEnabledRequest request) throws ProjectNotFoundException, ProjectIsDisabled {

        Optional<Project> optionalProject = this.projectRepository.findById(request.id());
        if (optionalProject.isEmpty()) {
            throw new ProjectNotFoundException("El proyecto no existe");
        }

        Project project = optionalProject.get();

        if(request.enable()==project.getIsEnabled()){
            if(request.enable()){
                throw new ProjectIsDisabled("El proyecto ya está habilitado");
            } else {
                throw new ProjectIsDisabled("El proyecto ya está deshabilitado");
            }
        }

        project.setIsEnabled(request.enable());

        List<Case> cases = this.caseRepository.findByFkProject(project.getId());
        for (Case c : cases) {
            c.setIsEnabled(request.enable());
        }

        this.caseRepository.saveAll(cases);
        Project updated = this.projectRepository.save(project);

        return new ProjectResponseWithoutUser(updated);
    }
}
