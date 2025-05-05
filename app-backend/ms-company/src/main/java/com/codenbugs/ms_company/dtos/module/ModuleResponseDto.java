package com.codenbugs.ms_company.dtos.module;

public record ModuleResponseDto(
        Integer moduleId,
        String moduleName,
        String modulePath,
        Integer pageId,
        String pageName,
        String pagePath) {

}
