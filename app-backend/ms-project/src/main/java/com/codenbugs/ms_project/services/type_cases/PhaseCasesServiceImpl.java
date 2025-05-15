package com.codenbugs.ms_project.services.type_cases;

import com.codenbugs.ms_project.dtos.cases.PhasesCaseRequest;
import com.codenbugs.ms_project.exceptions.typeCases.PhaseCasesException;
import com.codenbugs.ms_project.model.cases.CasePhase;
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
@Transactional(rollbackOn = Exception.class)
public class PhaseCasesServiceImpl implements PhaseCasesService {

    private final PhaseCasesRepository phaseCasesRepository;


    @Override
    public List<CasePhase> findByCaseType(Integer caseId) {
        return phaseCasesRepository.findByFkCaseType(caseId);
    }

    @Override
    public CasePhase save(PhasesCaseRequest phasesCase, Integer caseId, Integer nextPhaseId) {
        CasePhase newCase = new CasePhase();
        newCase.setFkCaseType(caseId);
        newCase.setName(phasesCase.name());
        newCase.setNextPhase(nextPhaseId);

        return phaseCasesRepository.save(newCase);
    }

    @Override
    public void deleteAllByFKCaseType(Integer caseId) {
        this.phaseCasesRepository.deleteByFkCaseType(caseId);
    }


}
