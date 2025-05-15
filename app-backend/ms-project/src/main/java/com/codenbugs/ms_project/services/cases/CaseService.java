package com.codenbugs.ms_project.services.cases;

import com.codenbugs.ms_project.dtos.cases.*;
import com.codenbugs.ms_project.exceptions.cases.CaseException;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFoundException;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFoundException;
import com.codenbugs.ms_project.exceptions.user.UserIsDisabled;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;

import java.util.List;


public interface CaseService {

    CaseResponseDto saveCase(CaseRequestDto request) throws ProjectNotFoundException, ProjectIsDisabled, UserNotFoundException, UserIsDisabled, CaseException;

    CaseResponseDto getCaseById(Integer id) throws CaseNotFoundException;

    CaseResponseDto updateCase(CaseRequestDto request) throws CaseIsDisabled, CaseException;

    CaseResponseDto cancelCase(CaseCancelledRequestDto request) throws CaseNotFoundException, CaseIsDisabled;

    List<CaseResponseDto> getCasesByProjectId(Integer projectId);

    List<CaseWithUserDto> getActiveCasesByProject(Integer fkProject);

    List<CaseResponseDto> getCasesByIsCancelled(Boolean isCancelled);

    CaseDetailsResponse getCaseDetails(Integer id) throws CaseNotFoundException;

}
