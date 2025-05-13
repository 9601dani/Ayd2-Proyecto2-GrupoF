package com.codenbugs.ms_project.services.cases;

import com.codenbugs.ms_project.dtos.cases.CaseCancelledRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseRequestDto;
import com.codenbugs.ms_project.dtos.cases.CaseResponseDto;
import com.codenbugs.ms_project.exceptions.cases.CaseException;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFound;
import com.codenbugs.ms_project.exceptions.project.ProjectIsDisabled;
import com.codenbugs.ms_project.exceptions.project.ProjectNotFound;

import java.util.List;


public interface CaseService {

    CaseResponseDto saveCase(CaseRequestDto request) throws ProjectNotFound, ProjectIsDisabled;

    CaseResponseDto getCaseById(Integer id) throws CaseNotFound;

    CaseResponseDto updateCase(CaseRequestDto request) throws CaseIsDisabled, CaseNotFound;

    CaseResponseDto cancelCase(CaseCancelledRequestDto request) throws CaseNotFound, CaseIsDisabled;

    List<CaseResponseDto> getCasesByProjectId(Integer projectId);


}
