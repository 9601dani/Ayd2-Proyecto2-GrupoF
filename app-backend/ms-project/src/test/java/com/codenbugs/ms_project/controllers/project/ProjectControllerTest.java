package com.codenbugs.ms_project.controllers.project;

import com.codenbugs.ms_project.dtos.project.*;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.services.project.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProjectService projectService;

    private final Integer ID = 1;
    private final String NAME = "Proyecto X";
    private final String DESCRIPTION = "Descripcion del proyecto";
    private final Boolean ENABLED = true;
    private final Integer FK_USER = 5;
    private final String USERNAME = "username";
    private final Integer ROLE = 1;
    private final String PHOTO = "photo";
    private final BigDecimal SALARY = new BigDecimal("100");

    private ProjectRequest projectRequest;
    private ProjectEnabledRequest projectEnabledRequest;
    private ProjectResponseWithoutUser responseWithoutUser;
    private ProjectResponse responseWithUser;

    @BeforeEach
    void setUp() {
        projectRequest = new ProjectRequest(ID, NAME, DESCRIPTION, FK_USER);
        projectEnabledRequest = new ProjectEnabledRequest(ID, ENABLED);
        responseWithoutUser = new ProjectResponseWithoutUser(ID, NAME, DESCRIPTION, ENABLED, FK_USER);
        responseWithUser = new ProjectResponse(ID, NAME, DESCRIPTION, ENABLED,
                new UserResponse(FK_USER, USERNAME,ROLE,PHOTO,SALARY, ENABLED));
    }

    @Test
    void createProject() throws Exception {
        when(projectService.saveProject(any(ProjectRequest.class))).thenReturn(responseWithoutUser);

        mockMvc.perform(post("/v1/projects/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseWithoutUser)));
    }

    @Test
    void updateProject() throws Exception {
        when(projectService.updateProject(any(ProjectRequest.class))).thenReturn(responseWithoutUser);

        mockMvc.perform(put("/v1/projects/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseWithoutUser)));
    }

    @Test
    void getProjectById() throws Exception {
        when(projectService.getById(ID)).thenReturn(responseWithUser);

        mockMvc.perform(get("/v1/projects/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseWithUser)));
    }

    @Test
    void getAllProjects() throws Exception {
        List<ProjectResponseWithoutUser> projects = List.of(responseWithoutUser);
        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/v1/projects/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projects)));
    }

    @Test
    void updateEnableProject() throws Exception {
        when(projectService.updateEnabled(any(ProjectEnabledRequest.class))).thenReturn(responseWithoutUser);

        mockMvc.perform(put("/v1/projects/update/enable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectEnabledRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseWithoutUser)));
    }
}
