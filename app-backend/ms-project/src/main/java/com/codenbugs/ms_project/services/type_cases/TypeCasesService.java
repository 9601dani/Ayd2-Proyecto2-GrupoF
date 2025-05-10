package com.codenbugs.ms_project.services.type_cases;

import com.codenbugs.ms_project.dtos.cases.TypeCasesRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesResponse;
import com.codenbugs.ms_project.exceptions.typeCases.NameTypeCaseAlreadyExist;

import java.util.List;

public interface TypeCasesService {

    List<TypeCasesResponse> getAllTypeCases();
    TypeCasesResponse create(TypeCasesRequest typeCasesRequest) throws NameTypeCaseAlreadyExist;

}
