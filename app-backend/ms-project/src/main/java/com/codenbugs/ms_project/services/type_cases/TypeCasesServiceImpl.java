package com.codenbugs.ms_project.services.type_cases;

import com.codenbugs.ms_project.dtos.cases.PhasesCaseRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesResponse;
import com.codenbugs.ms_project.exceptions.typeCases.NameTypeCaseAlreadyExist;
import com.codenbugs.ms_project.exceptions.typeCases.TypeCaseNotFoundException;
import com.codenbugs.ms_project.exceptions.typeCases.TypeCasesException;
import com.codenbugs.ms_project.model.cases.CasePhase;
import com.codenbugs.ms_project.model.cases.TypeCase;
import com.codenbugs.ms_project.repositories.typeCases.TypeCasesRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
@Transactional(rollbackOn = TypeCasesException.class)
public class TypeCasesServiceImpl implements TypeCasesService {

    private final TypeCasesRepository typeCasesRepository;
    private final PhaseCasesService phaseCasesService;

    @Override
    public List<TypeCasesResponse> getAllTypeCases() {
        List<TypeCase> cases = typeCasesRepository.findAll();
        List<TypeCasesResponse> responses = new ArrayList<>();

        for (TypeCase type : cases) {
            List<CasePhase> unorderedPhases = this.phaseCasesService.findByCaseType(type.getId());

            Map<Integer, CasePhase> idToPhase = unorderedPhases.stream()
                    .collect(Collectors.toMap(CasePhase::getId, p -> p));

            Map<Integer, CasePhase> nextToCurrent = unorderedPhases.stream()
                    .filter(p -> p.getNextPhase() != null)
                    .collect(Collectors.toMap(CasePhase::getNextPhase, p -> p));

            CasePhase current = unorderedPhases.stream()
                    .filter(p -> p.getNextPhase() == null)
                    .findFirst()
                    .orElse(null);

            List<CasePhase> orderedPhases = new ArrayList<>();
            while (current != null) {
                orderedPhases.add(0, current);
                current = nextToCurrent.get(current.getId());
            }

            responses.add(new TypeCasesResponse(type, orderedPhases));
        }

        return responses;
    }

    @Override
    public TypeCasesResponse create(TypeCasesRequest typeCasesRequest) throws NameTypeCaseAlreadyExist {

        TypeCase typeCase = this.typeCasesRepository.findByName(typeCasesRequest.name());

        if(typeCase != null) {
          throw  new NameTypeCaseAlreadyExist("El nombre de caso : '"+typeCasesRequest.name()+"' ya existe, cambia el nombre del caso");
        }

        TypeCase newTypeCase = new TypeCase();
        newTypeCase.setName(typeCasesRequest.name());
        newTypeCase.setDescription(typeCasesRequest.description());

        this.typeCasesRepository.save(newTypeCase);

        List<CasePhase> savedPhases = new ArrayList<>();
        Integer nextPhaseId = null;

        List<PhasesCaseRequest> reversedPhases = new ArrayList<>(typeCasesRequest.phases());
        Collections.reverse(reversedPhases);

        for (PhasesCaseRequest phaseReq : reversedPhases) {
            CasePhase saved = this.phaseCasesService.save(phaseReq, newTypeCase.getId(), nextPhaseId);

            if (saved == null || saved.getId() == null) {
                throw new IllegalStateException("La fase no pudo ser guardada correctamente.");
            }

            savedPhases.add(saved);
            nextPhaseId = saved.getId();
        }
        Collections.reverse(savedPhases);

        return new TypeCasesResponse(newTypeCase, savedPhases);
    }

    @Override
    public TypeCasesResponse update(Integer id, TypeCasesRequest typeCasesRequest) throws TypeCasesException {
        TypeCase existingTypeCase = this.typeCasesRepository.findById(id).
                orElseThrow(()-> new TypeCaseNotFoundException("El Tipo de Caso con id : "+id+ " No existe "));

        TypeCase byName = this.typeCasesRepository.findByName(typeCasesRequest.name());
        if (byName != null && !byName.getId().equals(id)) {
            throw new NameTypeCaseAlreadyExist("Ya existe un Tipo de Caso con el nombre '" + typeCasesRequest.name() + "'");
        }

        existingTypeCase.setName(typeCasesRequest.name());
        existingTypeCase.setDescription(typeCasesRequest.description());

        this.typeCasesRepository.save(existingTypeCase);

        this.phaseCasesService.deleteAllByFKCaseType(existingTypeCase.getId());

        List<CasePhase> savedPhases = new ArrayList<>();
        Integer nextPhaseId = null;

        List<PhasesCaseRequest> reversedPhases = new ArrayList<>(typeCasesRequest.phases());
        Collections.reverse(reversedPhases);

        for (PhasesCaseRequest phaseReq : reversedPhases) {
            CasePhase saved = this.phaseCasesService.save(phaseReq, id, nextPhaseId);
            savedPhases.add(saved);
            nextPhaseId = saved.getId();
        }
        Collections.reverse(savedPhases);

        return new TypeCasesResponse(existingTypeCase, savedPhases);

    }
}
