package com.codenbugs.ms_project.services.project;

import com.codenbugs.ms_project.dtos.project.ProjectRequest;
import com.codenbugs.ms_project.dtos.project.ProjectResponse;
import com.codenbugs.ms_project.exceptions.ProjectAlreadyExists;

public interface ProjectService {

    ProjectResponse saveProject(ProjectRequest request) throws ProjectAlreadyExists;

    ProjectResponse updateProject(ProjectRequest request);

    ProjectResponse getById(Integer id);

}
