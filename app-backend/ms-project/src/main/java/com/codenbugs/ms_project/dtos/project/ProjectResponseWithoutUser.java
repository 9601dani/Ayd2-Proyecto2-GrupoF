package com.codenbugs.ms_project.dtos.project;

import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.model.project.Project;

public record ProjectResponseWithoutUser(Integer id, String name, String description, Boolean isEnabled, Integer fkUser) {

    public ProjectResponseWithoutUser(Project project) {
        this(project.getId(), project.getName(), project.getDescription(), project.getIsEnabled(), project.getFK_User());
    }
}
