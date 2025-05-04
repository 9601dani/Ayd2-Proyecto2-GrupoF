package com.codenbugs.ms_company.service.module;


import com.codenbugs.ms_company.dtos.module.ModuleResponseDto;
import com.codenbugs.ms_company.model.module.Module;
import com.codenbugs.ms_company.model.module.Page;
import com.codenbugs.ms_company.repositories.module.PageRepository;
import com.codenbugs.ms_company.services.module.PageService;
import com.codenbugs.ms_company.services.module.PageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import static org.mockito.Mockito.when;

public class PageServiceTest {

    private PageService pageService;

    @Mock
    private PageRepository pageRepository;

    private final Integer ROLE_ID = 1;
    private final Integer MODULE_ID = 1;
    private final String MODULE_NAME = "Module Name";
    private final String MODULE_PATH = "Module Path";

    private final Integer PAGE_ID = 1;
    private final String PAGE_NAME = "Page";
    private final String PAGE_PATH = "/path";

    private Module module;
    private Page page;
    private ModuleResponseDto moduleResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        pageService = new PageServiceImpl(pageRepository);

        module = new Module();
        module.setId(MODULE_ID);
        module.setName(MODULE_NAME);
        module.setPath(MODULE_PATH);

        page = new Page();
        page.setId(PAGE_ID);
        page.setName(PAGE_NAME);
        page.setPath(PAGE_PATH);

        moduleResponseDto = new ModuleResponseDto(MODULE_ID,MODULE_NAME,MODULE_PATH,PAGE_ID, PAGE_NAME, PAGE_PATH);
    }

    @Test
    public void getPagesSuccesfully() {

        List<ModuleResponseDto> modules = List.of(moduleResponseDto);
        when(this.pageRepository.findPagesByRoleId(ROLE_ID)).thenReturn(modules);

        List<ModuleResponseDto> actual = this.pageRepository.findPagesByRoleId(ROLE_ID);

        assertEquals(modules.size(), actual.size());
        assertEquals(modules.get(0), actual.get(0));

    }
}
