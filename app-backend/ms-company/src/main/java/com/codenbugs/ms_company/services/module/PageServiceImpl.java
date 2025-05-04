package com.codenbugs.ms_company.services.module;

import com.codenbugs.ms_company.dtos.module.ModuleResponseDto;
import com.codenbugs.ms_company.repositories.module.PageRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;

    @Override
    public List<ModuleResponseDto> getPagesByRole(Integer roleId) {
        return pageRepository.findPagesByRoleId(roleId);
    }
}
