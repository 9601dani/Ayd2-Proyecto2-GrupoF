package com.codenbugs.ms_project.dtos.project;

public record ProjectRequest(Integer id, String name, String description, Integer fkUser) {
}
