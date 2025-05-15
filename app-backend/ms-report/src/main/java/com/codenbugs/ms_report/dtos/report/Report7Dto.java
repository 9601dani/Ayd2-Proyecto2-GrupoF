package com.codenbugs.ms_report.dtos.report;

public record Report7Dto(
        Integer id, String name, String description, Boolean isEnabled, Integer fkUser, String username
) {
}
