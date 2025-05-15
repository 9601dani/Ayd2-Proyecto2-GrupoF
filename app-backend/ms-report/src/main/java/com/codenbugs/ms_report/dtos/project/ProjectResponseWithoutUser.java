package com.codenbugs.ms_report.dtos.project;

public record ProjectResponseWithoutUser(Integer id, String name, String description, Boolean isEnabled, Integer fkUser) {

}
