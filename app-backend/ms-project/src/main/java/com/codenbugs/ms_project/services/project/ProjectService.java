package com.codenbugs.ms_project.services.project;

import com.codenbugs.ms_project.dtos.project.ProjectRequest;
import com.codenbugs.ms_project.dtos.project.ProjectResponse;
import com.codenbugs.ms_project.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_project.exceptions.ProjectAlreadyExists;
import com.codenbugs.ms_project.exceptions.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.ProjectNotFound;
import com.codenbugs.ms_project.exceptions.UserNotFoundException;

import java.util.List;

public interface ProjectService {

    ProjectResponseWithoutUser saveProject(ProjectRequest request) throws ProjectAlreadyExists;

    ProjectResponseWithoutUser updateProject(ProjectRequest request) throws ProjectNotFound, ProjectIsDisabled;

    ProjectResponse getById(Integer id) throws ProjectNotFound, UserNotFoundException;

    List<ProjectResponseWithoutUser> getAllProjects();
}
