package com.codenbugs.ms_project.services.cases;

import com.codenbugs.ms_project.dtos.cases.*;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.cases.CasePhaseNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;

import java.util.List;

public interface HistoryCasePhaseService {

    List<HistoryCaseWithCaseDto> getAllWithCaseInfo();

    CasePhaseResponse getNextPhase(Integer id) throws CasePhaseNotFoundException;

    HistoryCaseResponseDto updateCasePhase(HistoryCaseRequest request) throws CasePhaseNotFoundException, CaseNotFoundException;

    HistoryCaseResponseDto saveNextPhase(NextPhaseRequest request) throws CasePhaseNotFoundException, CaseNotFoundException, UserNotFoundException;

    void completeCase(Integer id) throws CaseNotFoundException;
}
