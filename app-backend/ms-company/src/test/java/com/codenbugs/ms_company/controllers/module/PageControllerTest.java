package com.codenbugs.ms_company.controllers.module;

import com.codenbugs.ms_company.dtos.module.ModuleResponseDto;
import com.codenbugs.ms_company.exceptions.UserNotFoundException;
import com.codenbugs.ms_company.services.module.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PageControllerTest {

    private final Integer USERID = 1;
    private final Integer MODULEID = 1;
    private final String MODULENAME = "moduleName";
    private final String MODULEPATH = "/module";
    private final Integer PAGEID = 1;
    private final String PAGENAME = "pageName";
    private final String PAGEPATH = "/page";

    @Mock
    private PageService pageService;

    @InjectMocks
    private PageController pageController;

    private ModuleResponseDto moduleDto;

    @BeforeEach
    void setUp() {
        moduleDto = new ModuleResponseDto(MODULEID, MODULENAME,MODULEPATH,PAGEID,PAGENAME,PAGEPATH);
    }

    @Test
    void testGetPagesByRoleReturnsPages() throws UserNotFoundException {
        // Arrange
        List<ModuleResponseDto> modules = List.of(moduleDto);
        when(pageService.getPagesByRole(USERID)).thenReturn(modules);

        // Act
        ResponseEntity<List<ModuleResponseDto>> response = pageController.getPagesByRole(USERID);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(MODULENAME, response.getBody().get(0).moduleName());
    }

    @Test
    void testGetPagesByRoleThrowsException() throws UserNotFoundException {
        // Arrange
        when(pageService.getPagesByRole(USERID)).thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> pageController.getPagesByRole(USERID));
    }

}
