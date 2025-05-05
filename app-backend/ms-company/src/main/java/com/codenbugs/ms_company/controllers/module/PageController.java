package com.codenbugs.ms_company.controllers.module;

import com.codenbugs.ms_company.dtos.module.ModuleResponseDto;
import com.codenbugs.ms_company.exceptions.UserNotFoundException;
import com.codenbugs.ms_company.services.module.PageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/pages")
@AllArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ModuleResponseDto>> getPagesByRole(@PathVariable Integer userId) throws UserNotFoundException {
        return ResponseEntity.ok(pageService.getPagesByRole(userId));
    }
}
