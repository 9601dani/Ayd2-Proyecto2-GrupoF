package com.codenbugs.ms_project.services.type_cases;

import com.codenbugs.ms_project.dtos.cases.PhasesCaseRequest;
import com.codenbugs.ms_project.exceptions.typeCases.PhaseCasesException;
import com.codenbugs.ms_project.model.cases.PhasesCase;
import com.codenbugs.ms_project.repositories.typeCases.PhaseCasesRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Getter
@Setter
@Transactional(rollbackOn = PhaseCasesException.class)
public class PhaseCasesServiceImpl implements PhaseCasesService {
    private final PhaseCasesRepository phaseCasesRepository;


    @Override
    public List<PhasesCase> findByCaseType(Integer caseId) {
        return phaseCasesRepository.findByFkCaseType(caseId);
    }

    @Override
    public PhasesCase save(PhasesCaseRequest phasesCase, Integer caseId, Integer nextPhaseId) {
        PhasesCase newCase = new PhasesCase();
        newCase.setFkCaseType(caseId);
        newCase.setName(phasesCase.name());
        newCase.setNextPhase(nextPhaseId);

        return phaseCasesRepository.save(newCase);
    }


}
