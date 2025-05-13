package com.codenbugs.ms_project.controllers.cases;


import com.codenbugs.ms_project.dtos.cases.*;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFound;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFound;
import com.codenbugs.ms_project.exceptions.user.UserIsDisabled;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import com.codenbugs.ms_project.services.cases.CaseService;
import com.codenbugs.ms_project.services.cases.HistoryCasePhaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/history")
@AllArgsConstructor
public class HistoryCasePhaseController {

    private final HistoryCasePhaseService historyCasePhaseService;

    @GetMapping("/all-cases")
    public List<HistoryCaseWithCaseDto> getAllWithCaseInfo() {
        return historyCasePhaseService.getAllWithCaseInfo();
    }
}
