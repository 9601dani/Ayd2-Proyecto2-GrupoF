package com.codenbugs.ms_project.controllers.project;

import com.codenbugs.ms_project.dtos.project.ProjectEnabledRequest;
import com.codenbugs.ms_project.dtos.project.ProjectRequest;
import com.codenbugs.ms_project.dtos.project.ProjectResponse;
import com.codenbugs.ms_project.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_project.dtos.report.Report1Dto;
import com.codenbugs.ms_project.dtos.report.TopProjectByCompletedCasesDto;
import com.codenbugs.ms_project.exceptions.project.ProjectAlreadyExists;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFound;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.services.project.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/save")
    public ResponseEntity<ProjectResponseWithoutUser> createProject(@RequestBody ProjectRequest projectRequest) throws ProjectAlreadyExists {
        ProjectResponseWithoutUser response = this.projectService.saveProject(projectRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ProjectResponseWithoutUser> updateProject(@RequestBody ProjectRequest projectRequest) throws  ProjectIsDisabled, ProjectNotFound {
        ProjectResponseWithoutUser response = this.projectService.updateProject(projectRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Integer id) throws ProjectNotFound, UserNotFoundException {
        ProjectResponse response = this.projectService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponseWithoutUser>> getAllProjects() {
        List<ProjectResponseWithoutUser> response = this.projectService.getAllProjects();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/enable")
    public ResponseEntity<ProjectResponseWithoutUser> updateEnableProject(@RequestBody ProjectEnabledRequest projectRequest) throws ProjectNotFound, ProjectIsDisabled {
        ProjectResponseWithoutUser response = this.projectService.updateEnabled(projectRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Reports
    @GetMapping("/report1")
    public List<Report1Dto> getProjectReport() {
        return projectService.gerReport1();
    }

    @GetMapping("/top-project-completed")
    public TopProjectByCompletedCasesDto getTopProjectByCompletedCases() {
        return projectService.getTopProjectByCompletedCases();
    }

}
