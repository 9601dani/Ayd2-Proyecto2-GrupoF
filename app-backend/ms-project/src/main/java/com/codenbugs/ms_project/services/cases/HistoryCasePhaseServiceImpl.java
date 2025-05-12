package com.codenbugs.ms_project.services.cases;


import com.codenbugs.ms_project.clients.UserRestClient;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseRequest;
import com.codenbugs.ms_project.dtos.cases.HistoryCaseResponseDto;
import com.codenbugs.ms_project.dtos.user.UserResponse;
import com.codenbugs.ms_project.exceptions.cases.CaseException;
import com.codenbugs.ms_project.exceptions.cases.CaseIsDisabled;
import com.codenbugs.ms_project.exceptions.cases.CaseNotFound;
import com.codenbugs.ms_project.exceptions.user.UserIsDisabled;
import com.codenbugs.ms_project.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_project.model.cases.Case;
import com.codenbugs.ms_project.model.cases.HistoryCasePhase;
import com.codenbugs.ms_project.repositories.cases.CaseRepository;
import com.codenbugs.ms_project.repositories.cases.HistoryCasePhaseRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional(rollbackOn = CaseException.class)
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class HistoryCasePhaseServiceImpl implements HistoryCasePhaseService {

    private final HistoryCasePhaseRepository historyCasePhaseRepository;
    private final UserRestClient userRestClient;
    private final CaseRepository caseRepository;

    @Override
    public HistoryCaseResponseDto save(HistoryCaseRequest request) throws UserNotFoundException, UserIsDisabled, CaseNotFound, CaseIsDisabled {

        UserResponse user = this.userRestClient.findById(request.fkUser());

        if(!user.isEnabled()){
            throw new UserIsDisabled("El usuario esta deshabilitado");
        }

        Optional<Case> optionalCase = this.caseRepository.findById(request.fkCase());

        if(optionalCase.isEmpty()){
            throw new CaseNotFound("El caso no existe");
        }

        Case caseModel = optionalCase.get();

        if(!caseModel.getIsEnabled()){
            throw new CaseIsDisabled("El case está deshabilitado");
        }

        if(caseModel.getIsCancelled()){
            throw new CaseIsDisabled("El caso está cancelado");
        }

        HistoryCasePhase historyCasePhase = new HistoryCasePhase();
        historyCasePhase.setFkCase(caseModel.getId());
        historyCasePhase.setFkUser(user.id());
        historyCasePhase.setFkCasePhase(request.fkCasePhase());
        historyCasePhase.setIsCompleted(false);
        historyCasePhase.setTimeSpent(BigDecimal.ZERO);

        HistoryCasePhase historySaved = historyCasePhaseRepository.save(historyCasePhase);

        return new HistoryCaseResponseDto(historySaved);
    }
}
