package com.codenbugs.ms_project.services.project;

import com.codenbugs.ms_project.dtos.project.ProjectEnabledRequest;
import com.codenbugs.ms_project.dtos.project.ProjectRequest;
import com.codenbugs.ms_project.dtos.project.ProjectResponse;
import com.codenbugs.ms_project.dtos.project.ProjectResponseWithoutUser;
import com.codenbugs.ms_project.exceptions.project.ProjectAlreadyExists;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;

import java.util.List;

public interface ProjectService {

    ProjectResponseWithoutUser saveProject(ProjectRequest request) throws ProjectAlreadyExists;

    ProjectResponseWithoutUser updateProject(ProjectRequest request) throws ProjectNotFoundException, ProjectIsDisabled;

    ProjectResponse getById(Integer id) throws ProjectNotFoundException, UserNotFoundException;

    List<ProjectResponseWithoutUser> getAllProjects();

    ProjectResponseWithoutUser updateEnabled(ProjectEnabledRequest request) throws ProjectNotFoundException, ProjectIsDisabled;

}
