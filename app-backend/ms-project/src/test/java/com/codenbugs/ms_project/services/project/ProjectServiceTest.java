package com.codenbugs.ms_project.services.project;

import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.project.*;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.project.ProjectAlreadyExists;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import com.codenbugs.ms_project.model.project.Project;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.project.ProjectRepository;
import com.codenbugs.ms_project.services.cases.HistoryCasePhaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

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
    private final Boolean IS_ENABLED = true;

    private final String USERNAME = "username";
    private final Integer CASE_ID = 101;
    private final String CASE_NAME = "Case A";
    private final String CASE_DESCRIPTION = "Description A";
    private final BigDecimal PROGRESS_PERCENTAGE = BigDecimal.valueOf(75.0);
    private final LocalDateTime LIMIT_DATE = LocalDateTime.now();
    private final String PHASE_NAME = "Initial Phase";

    private Project project;
    private ProjectResponse projectResponse;
    private ProjectResponseWithoutUser projectResponseWithoutUser;
    private UserResponse userResponse;
    private ProjectRequest projectRequest;

    @Mock
    private HistoryCasePhaseService historyCasePhaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        projectService = new ProjectServiceImpl(projectRepository, userRestClient, caseRepository, historyCasePhaseService);

        project = new Project();
        project.setId(PROJECT_ID);
        project.setName(PROJECT_NAME);
        project.setDescription(PROJECT_DESCRIPTION);
        project.setFK_User(USER_ID);
        project.setIsEnabled(PROJECT_ENABLED);

        userResponse = new UserResponse(USER_ID, USER_NAME, ROLE, PHOTO, SALARY_PER_HOUR, IS_ENABLED);

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
    public void updateProjectSuccesfully() throws ProjectNotFoundException, ProjectIsDisabled {

        project.setIsEnabled(true);
        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.of(project));

        when(this.projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseWithoutUser expect = new ProjectResponseWithoutUser(project);
        ProjectResponseWithoutUser actual = this.projectService.updateProject(projectRequest);

        assertEquals(expect, actual);
    }

    @Test
    public void updateProjectNotFound() throws ProjectNotFoundException, ProjectIsDisabled {

        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.updateProject(projectRequest));

    }

    @Test
    public void updateProjectDisabled() throws ProjectNotFoundException, ProjectIsDisabled {

        project.setIsEnabled(false);

        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.of(project));

        assertThrows(ProjectIsDisabled.class, () -> projectService.updateProject(projectRequest));

    }

    @Test
    public void getProjectByIdSuccesfully() throws ProjectNotFoundException, UserNotFoundException {

        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.of(project));

        when(this.userRestClient.findById(USER_ID)).thenReturn(userResponse);

        ProjectResponse expect = new ProjectResponse(project, userResponse);
        ProjectResponse actual = this.projectService.getById(PROJECT_ID);

        assertEquals(expect, actual);
    }

    @Test
    public void getProjectByIdNotFound() throws ProjectNotFoundException, UserNotFoundException {

        when(this.projectRepository.findById(projectRequest.id())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getById(PROJECT_ID));

    }

    @Test
    public void getAllProjectsSuccesfully(){

        List<Project> projects = List.of(project);
        when(this.projectRepository.findAll()).thenReturn(projects);

        List<ProjectResponseWithoutUser> expect = List.of(projectResponseWithoutUser);
        List<ProjectResponseWithoutUser> actual = this.projectService.getAllProjects();
        assertEquals(expect.get(0), actual.get(0));
    }

    @Test
    public void updateEnabledProjectSuccessfully_Enable() throws ProjectNotFoundException, ProjectIsDisabled {
        project.setIsEnabled(false);
        ProjectEnabledRequest request = new ProjectEnabledRequest(PROJECT_ID, true);

        when(this.projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(this.caseRepository.findByFkProject(PROJECT_ID)).thenReturn(List.of());
        when(this.projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseWithoutUser actual = this.projectService.updateEnabled(request);

        assertTrue(actual.isEnabled());
    }

    @Test
    public void updateEnabledProjectSuccessfully_Disable() throws ProjectNotFoundException, ProjectIsDisabled {
        project.setIsEnabled(true);
        ProjectEnabledRequest request = new ProjectEnabledRequest(PROJECT_ID, false);

        when(this.projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(this.caseRepository.findByFkProject(PROJECT_ID)).thenReturn(List.of());
        when(this.projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseWithoutUser actual = this.projectService.updateEnabled(request);

        assertFalse(actual.isEnabled());
    }

    @Test
    public void updateEnabledProjectAlreadyEnabled() {
        project.setIsEnabled(true);
        ProjectEnabledRequest request = new ProjectEnabledRequest(PROJECT_ID, true);

        when(this.projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        assertThrows(ProjectIsDisabled.class, () -> this.projectService.updateEnabled(request));
    }

    @Test
    public void updateEnabledProjectAlreadyDisabled() {
        project.setIsEnabled(false);
        ProjectEnabledRequest request = new ProjectEnabledRequest(PROJECT_ID, false);

        when(this.projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        assertThrows(ProjectIsDisabled.class, () -> this.projectService.updateEnabled(request));
    }

    @Test
    public void updateEnabledNotFound() throws ProjectNotFoundException {
        
        when(this.projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getById(PROJECT_ID));

    }
    @Test
    void updateEnabledShouldThrowProjectNotFoundWhenProjectDoesNotExist() {
        // Arrange
        ProjectEnabledRequest request = new ProjectEnabledRequest(PROJECT_ID, PROJECT_ENABLED);
        when(projectRepository.findById(request.id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProjectNotFound.class, () -> {
            projectService.updateEnabled(request);
        });
    }

    @Test
    void updateEnabledshouldThrowProjectIsDisabledWhenAlreadyEnabled() {
        // Arrange
        ProjectEnabledRequest request = new ProjectEnabledRequest(PROJECT_ID, PROJECT_ENABLED);
        Project project = new Project();
        project.setId(PROJECT_ID);
        project.setIsEnabled(PROJECT_ENABLED);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        // Act & Assert
        ProjectIsDisabled ex = assertThrows(ProjectIsDisabled.class, () -> {
            projectService.updateEnabled(request);
        });

        assertEquals("El proyecto ya está habilitado", ex.getMessage());
    }

    @Test
    void updateEnabled_shouldThrowProjectIsDisabled_whenAlreadyDisabled() {
        // Arrange
        ProjectEnabledRequest request = new ProjectEnabledRequest(PROJECT_ID, !PROJECT_ENABLED);
        Project project = new Project();
        project.setId(PROJECT_ID);
        project.setIsEnabled(!PROJECT_ENABLED);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        // Act & Assert
        ProjectIsDisabled ex = assertThrows(ProjectIsDisabled.class, () -> {
            projectService.updateEnabled(request);
        });

        assertEquals("El proyecto ya está deshabilitado", ex.getMessage());
    }

    @Test
    void getActiveCasesByUsernameShouldReturnListOfActiveCases() throws UserNotFoundException {
        // Arrange
        UserResponse user = new UserResponse(USER_ID, USER_NAME, ROLE, PHOTO, SALARY_PER_HOUR, IS_ENABLED);

        HistoryCasePhase phase = new HistoryCasePhase();
        phase.setFkCase(CASE_ID);
        phase.setFkUser(USER_ID);
        phase.setPhaseName(PHASE_NAME);
        phase.setIsCompleted(false);

        Case caseModel = new Case();
        caseModel.setId(CASE_ID);
        caseModel.setName(CASE_NAME);
        caseModel.setDescription(CASE_DESCRIPTION);
        caseModel.setProgressPercentage(PROGRESS_PERCENTAGE);
        caseModel.setLimitDate(LIMIT_DATE);
        caseModel.setIsEnabled(true);
        caseModel.setIsCancelled(false);

        when(userRestClient.findByUsername(USERNAME)).thenReturn(user);
        when(historyCasePhaseService.findByFkUser(USER_ID)).thenReturn(List.of(phase));
        when(caseRepository.findAllById(Set.of(CASE_ID))).thenReturn(List.of(caseModel));

        // Act
        List<ActiveCaseReponse> result = projectService.getActiveCasesByUsername(USERNAME);

        // Assert
        assertEquals(1, result.size());
        ActiveCaseReponse response = result.get(0);
        assertEquals(CASE_ID, response.caseId());
        assertEquals(CASE_NAME, response.caseName());
        assertEquals(CASE_DESCRIPTION, response.description());
        assertEquals(PROGRESS_PERCENTAGE, response.progressPercentage());
        assertEquals(LIMIT_DATE, response.limitDate());
        assertEquals(PHASE_NAME, response.currentPhaseName());
    }

    @Test
    void getActiveCasesByUsernameShouldThrowUserNotFoundExceptionWhenUserIsNull() throws UserNotFoundException {
        // Arrange
        final String USERNAME = "nonexistent_user";

        when(userRestClient.findByUsername(USERNAME)).thenReturn(null);

        // Act & Assert
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
            projectService.getActiveCasesByUsername(USERNAME);
        });

        assertEquals("El usuario no existe", ex.getMessage());
    }



}
