package com.codenbugs.ms_project.dtos.project;

import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.model.project.Project;

public record ProjectResponse(Integer id, String name, String description, Boolean isEnabled, UserResponse user) {

    public ProjectResponse(Project project, UserResponse user) {
        this(project.getId(), project.getName(), project.getDescription(), project.getIsEnabled(), user);
    }

}
