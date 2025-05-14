package com.codenbugs.ms_project.services.project;

import com.codenbugs.ms_project.dtos.project.*;
import com.codenbugs.ms_project.exceptions.project.ProjectAlreadyExists;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFound;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;

import java.util.List;

public interface ProjectService {

    ProjectResponseWithoutUser saveProject(ProjectRequest request) throws ProjectAlreadyExists;

    ProjectResponseWithoutUser updateProject(ProjectRequest request) throws ProjectNotFound, ProjectIsDisabled;

    ProjectResponse getById(Integer id) throws ProjectNotFound, UserNotFoundException;

    List<ProjectResponseWithoutUser> getAllProjects();

    ProjectResponseWithoutUser updateEnabled(ProjectEnabledRequest request) throws ProjectNotFound, ProjectIsDisabled;

    List<ActiveCaseReponse> getActiveCasesByUsername(String username) throws UserNotFoundException;

}
