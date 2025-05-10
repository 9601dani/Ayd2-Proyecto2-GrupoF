package com.codenbugs.ms_project.services;

import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.project.ProjectEnabledRequest;
import com.codenbugs.ms_project.dtos.project.ProjectRequest;
import com.codenbugs.ms_project.dtos.project.ProjectResponse;
import com.codenbugs.ms_project.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.project.ProjectAlreadyExists;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFound;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.project.Project;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import com.codenbugs.ms_project.services.project.ProjectService;
import com.codenbugs.ms_project.services.project.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Optional;

public class ProjectServiceTest {

    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRestClient userRestClient;

    @Mock
    private CaseRepository caseRepository;

    private final Integer PROJECT_ID = 1;
    private final String PROJECT_NAME = "Project Name";
    private final String PROJECT_DESCRIPTION = "Project Description";
    private final Boolean PROJECT_ENABLED = true;
    private final Integer USER_ID = 1;
    private final String USER_NAME = "User Name";
    private final Integer ROLE = 1;
    private final String PHOTO = "Photo";
    private final BigDecimal SALARY_PER_HOUR = BigDecimal.valueOf(1.0);

    private Project project;
    private ProjectResponse projectResponse;
    private ProjectResponseWithoutUser projectResponseWithoutUser;
    private UserResponse userResponse;
    private ProjectRequest projectRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        projectService = new ProjectServiceImpl(projectRepository, userRestClient, caseRepository);

        project = new Project();
        project.setId(PROJECT_ID);
        project.setName(PROJECT_NAME);
        project.setDescription(PROJECT_DESCRIPTION);
        project.setFK_User(USER_ID);
        project.setIsEnabled(PROJECT_ENABLED);

        userResponse = new UserResponse(USER_ID, USER_NAME, ROLE, PHOTO, SALARY_PER_HOUR);

        projectResponse = new ProjectResponse(project, userResponse);

        projectResponseWithoutUser = new ProjectResponseWithoutUser(project);

        projectRequest = new ProjectRequest(PROJECT_ID, PROJECT_NAME, PROJECT_DESCRIPTION, USER_ID);
    }

    @Test
    public void saveProjectSuccesfully() throws ProjectAlreadyExists {

        when(this.projectRepository.findByName(projectRequest.name())).thenReturn(Optional.empty());

        when(this.projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseWithoutUser expect = new ProjectResponseWithoutUser(project);
        ProjectResponseWithoutUser actual = this.projectService.saveProject(projectRequest);

        assertEquals(expect, actual);

    }

    @Test
    public void saveProjectAlreadyExist() throws ProjectAlreadyExists {

        when(this.projectRepository.findByName(projectRequest.name())).thenReturn(Optional.of(project));

        assertThrows(ProjectAlreadyExists.class, () -> projectService.saveProject(projectRequest));

    }

    @Test
    public void updateProjectSuccesfully() throws ProjectNotFound, ProjectIsDisabled {

        project.setIsEnabled(true);
        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.of(project));

        when(this.projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseWithoutUser expect = new ProjectResponseWithoutUser(project);
        ProjectResponseWithoutUser actual = this.projectService.updateProject(projectRequest);

        assertEquals(expect, actual);
    }

    @Test
    public void updateProjectNotFound() throws ProjectNotFound, ProjectIsDisabled {

        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFound.class, () -> projectService.updateProject(projectRequest));

    }

    @Test
    public void updateProjectDisabled() throws ProjectNotFound, ProjectIsDisabled {

        project.setIsEnabled(false);

        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.of(project));

        assertThrows(ProjectIsDisabled.class, () -> projectService.updateProject(projectRequest));

    }

    @Test
    public void getProjectByIdSuccesfully() throws ProjectNotFound, UserNotFoundException {

        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.of(project));

        when(this.userRestClient.findById(USER_ID)).thenReturn(userResponse);

        ProjectResponse expect = new ProjectResponse(project, userResponse);
        ProjectResponse actual = this.projectService.getById(PROJECT_ID);

        assertEquals(expect, actual);
    }

    @Test
    public void getProjectByIdNotFound() throws ProjectNotFound, UserNotFoundException {

        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFound.class, () -> projectService.getById(PROJECT_ID));

    }

    @Test
    public void getAllProjectsSuccesfully(){

        List<Project> projects = List.of(project);
        when(this.projectRepository.findAll()).thenReturn(projects);

        List<ProjectResponseWithoutUser> expect = List.of(projectResponseWithoutUser);
        List<ProjectResponseWithoutUser> actual = this.projectService.getAllProjects();
        assertEquals(expect.get(0), actual.get(0));
    }

/*    @Test
    public void updateEnabledSuccesfully() throws ProjectNotFound {

        ProjectEnabledRequest request = new ProjectEnabledRequest(PROJECT_ID, PROJECT_ENABLED);

        when(this.projectRepository.far
        indById(projectRequest.id())).thenReturn(Optional.of(project));

        when(this.projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseWithoutUser expect = new ProjectResponseWithoutUser(project);

        ProjectResponseWithoutUser actual = this.projectService.updateEnabled(request);

        assertEquals(expect, actual);

    }*/

    @Test
    public void updateEnabledNotFound() throws ProjectNotFound {
        
        when(this.projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFound.class, () -> projectService.getById(PROJECT_ID));

    }



}
