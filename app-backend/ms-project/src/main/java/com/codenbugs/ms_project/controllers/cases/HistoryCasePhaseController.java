package com.codenbugs.ms_project.controllers.cases;


import com.codenbugs.ms_project.dtos.cases.*;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.cases.CasePhaseNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.services.cases.HistoryCasePhaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/histories")
@AllArgsConstructor
public class HistoryCasePhaseController {

    private final HistoryCasePhaseService historyCasePhaseService;

    @GetMapping("/all-cases")
    public List<HistoryCaseWithCaseDto> getAllWithCaseInfo() {
        return historyCasePhaseService.getAllWithCaseInfo();
    }

    @GetMapping("/next/{id}")
    public ResponseEntity<CasePhaseResponse> getNextPhase(@PathVariable Integer id) throws CasePhaseNotFoundException {
        CasePhaseResponse response = this.historyCasePhaseService.getNextPhase(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<HistoryCaseResponseDto> updateCasePhase(@RequestBody HistoryCaseRequest historyCaseRequest) throws CasePhaseNotFoundException, CaseNotFoundException {
        HistoryCaseResponseDto response = this.historyCasePhaseService.updateCasePhase(historyCaseRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<HistoryCaseResponseDto> saveNextPhase(@RequestBody NextPhaseRequest request) throws UserNotFoundException, CasePhaseNotFoundException, CaseNotFoundException {
        HistoryCaseResponseDto response = this.historyCasePhaseService.saveNextPhase(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<Void> completeCase(@PathVariable Integer id) throws CaseNotFoundException {
        this.historyCasePhaseService.completeCase(id);
        return ResponseEntity.ok().build();
    }
}
