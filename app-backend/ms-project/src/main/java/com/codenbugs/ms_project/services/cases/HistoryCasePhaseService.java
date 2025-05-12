package com.codenbugs.ms_project.services.cases;

import com.codenbugs.ms_project.dtos.cases.HistoryCaseRequest;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseResponseDto;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFound;
import com.codenbugs.ms_project.exceptions.user.UserIsDisabled;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;

public interface HistoryCasePhaseService {

    public HistoryCaseResponseDto save(HistoryCaseRequest dto) throws UserNotFoundException, UserIsDisabled, CaseNotFound, CaseIsDisabled;

}
