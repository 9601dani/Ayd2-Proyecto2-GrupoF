package com.codenbugs.ms_company.services.module;

import com.codenbugs.ms_company.dtos.module.ModuleResponseDto;

import java.util.List;

public interface PageService {

    public List<ModuleResponseDto> getPagesByRole(Integer roleId);
}
