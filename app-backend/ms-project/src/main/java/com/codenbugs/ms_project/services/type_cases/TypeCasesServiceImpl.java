package com.codenbugs.ms_project.services.type_cases;

import com.codenbugs.ms_project.dtos.cases.PhasesCaseRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesResponse;
import com.codenbugs.ms_project.exceptions.typeCases.NameTypeCaseAlreadyExist;
import com.codenbugs.ms_project.exceptions.typeCases.TypeCaseNotFoundException;
import com.codenbugs.ms_project.exceptions.typeCases.TypeCasesException;
import com.codenbugs.ms_project.model.cases.PhasesCase;
import com.codenbugs.ms_project.model.cases.TypesCase;
import com.codenbugs.ms_project.repositories.typeCases.PhaseCasesRepository;
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
        List<TypesCase> cases = typeCasesRepository.findAll();
        List<TypeCasesResponse> responses = new ArrayList<>();

        for (TypesCase type : cases) {
            List<PhasesCase> unorderedPhases = this.phaseCasesService.findByCaseType(type.getId());

            Map<Integer, PhasesCase> idToPhase = unorderedPhases.stream()
                    .collect(Collectors.toMap(PhasesCase::getId, p -> p));

            Map<Integer, PhasesCase> nextToCurrent = unorderedPhases.stream()
                    .filter(p -> p.getNextPhase() != null)
                    .collect(Collectors.toMap(PhasesCase::getNextPhase, p -> p));

            PhasesCase current = unorderedPhases.stream()
                    .filter(p -> p.getNextPhase() == null)
                    .findFirst()
                    .orElse(null);

            List<PhasesCase> orderedPhases = new ArrayList<>();
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

        TypesCase typeCase = this.typeCasesRepository.findByName(typeCasesRequest.name());

        if(typeCase != null) {
          throw  new NameTypeCaseAlreadyExist("El nombre de caso : '"+typeCasesRequest.name()+"' ya existe, cambia el nombre del caso");
        }

        TypesCase newTypeCase = new TypesCase();
        newTypeCase.setName(typeCasesRequest.name());
        newTypeCase.setDescription(typeCasesRequest.description());

        this.typeCasesRepository.save(newTypeCase);

        List<PhasesCase> savedPhases = new ArrayList<>();
        Integer nextPhaseId = null;

        List<PhasesCaseRequest> reversedPhases = new ArrayList<>(typeCasesRequest.phases());
        Collections.reverse(reversedPhases);

        for (PhasesCaseRequest phaseReq : reversedPhases) {
            PhasesCase saved = this.phaseCasesService.save(phaseReq, newTypeCase.getId(), nextPhaseId);
            savedPhases.add(saved);
            nextPhaseId = saved.getId();
        }
        Collections.reverse(savedPhases);

        return new TypeCasesResponse(newTypeCase, savedPhases);
    }

    @Override
    public TypeCasesResponse update(Integer id, TypeCasesRequest typeCasesRequest) throws TypeCasesException {
        TypesCase existingTypeCase = this.typeCasesRepository.findById(id).
                orElseThrow(()-> new TypeCaseNotFoundException("El Tipo de Caso con id : "+id+ " No existe "));

        TypesCase byName = this.typeCasesRepository.findByName(typeCasesRequest.name());
        if (byName != null && !byName.getId().equals(id)) {
            throw new NameTypeCaseAlreadyExist("Ya existe un Tipo de Caso con el nombre '" + typeCasesRequest.name() + "'");
        }

        existingTypeCase.setName(typeCasesRequest.name());
        existingTypeCase.setDescription(typeCasesRequest.description());

        this.typeCasesRepository.save(existingTypeCase);

        this.phaseCasesService.deleteAllByFKCaseType(existingTypeCase.getId());

        List<PhasesCase> savedPhases = new ArrayList<>();
        Integer nextPhaseId = null;

        List<PhasesCaseRequest> reversedPhases = new ArrayList<>(typeCasesRequest.phases());
        Collections.reverse(reversedPhases);

        for (PhasesCaseRequest phaseReq : reversedPhases) {
            PhasesCase saved = this.phaseCasesService.save(phaseReq, id, nextPhaseId);
            savedPhases.add(saved);
            nextPhaseId = saved.getId();
        }
        Collections.reverse(savedPhases);

        return new TypeCasesResponse(existingTypeCase, savedPhases);

    }
}
