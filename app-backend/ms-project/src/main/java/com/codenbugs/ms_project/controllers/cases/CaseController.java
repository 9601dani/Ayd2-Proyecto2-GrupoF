package com.codenbugs.ms_project.controllers.cases;

import com.codenbugs.ms_project.dtos.cases.*;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserIsDisabled;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.services.cases.CaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cases")
@AllArgsConstructor
public class CaseController {

    private final CaseService caseService;

    @PostMapping("/save")
    public ResponseEntity<CaseResponseDto> createCase(@RequestBody CaseRequestDto caseRequestDto) throws ProjectIsDisabled, ProjectNotFoundException, UserNotFoundException, UserIsDisabled {
        CaseResponseDto responseDto = this.caseService.saveCase(caseRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaseResponseDto> getCaseById(@PathVariable Integer id) throws CaseNotFoundException {
        CaseResponseDto responseDto = this.caseService.getCaseById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<CaseResponseDto> updateCase(@RequestBody CaseRequestDto caseRequestDto) throws CaseNotFoundException, CaseIsDisabled {
        CaseResponseDto responseDto = this.caseService.updateCase(caseRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/update/cancel")
    public ResponseEntity<CaseResponseDto> updateCancelCase(@RequestBody CaseCancelledRequestDto caseRequestDto) throws CaseNotFoundException, CaseIsDisabled {
        CaseResponseDto responseDto = this.caseService.cancelCase(caseRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/all/{fkProject}")
    public ResponseEntity<List<CaseResponseDto>> getAllCasesByFkProject(@PathVariable Integer fkProject)  {
        List<CaseResponseDto> responseDtos = this.caseService.getCasesByProjectId(fkProject);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/cancel/{isCancelled}")
    public ResponseEntity<List<CaseResponseDto>> getCasesByIsCancelled(@PathVariable Boolean isCancelled)  {
        List<CaseResponseDto> responseDtos = this.caseService.getCasesByIsCancelled(isCancelled);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/active/{projectId}")
    public List<CaseWithUserDto> getActiveCasesByProject(@PathVariable Integer projectId) {
        return caseService.getActiveCasesByProject(projectId);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<CaseDetailsResponse> getCaseDetails(@PathVariable Integer id) throws CaseNotFoundException {
        CaseDetailsResponse caseDetailsResponse = this.caseService.getCaseDetails(id);
        return ResponseEntity.ok(caseDetailsResponse);
    }
}
