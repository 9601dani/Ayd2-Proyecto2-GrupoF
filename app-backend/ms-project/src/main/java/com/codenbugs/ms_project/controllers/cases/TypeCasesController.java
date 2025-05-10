package com.codenbugs.ms_project.controllers.cases;

import com.codenbugs.ms_project.dtos.cases.TypeCasesRequest;
import com.codenbugs.ms_project.dtos.cases.TypeCasesResponse;
import com.codenbugs.ms_project.exceptions.typeCases.NameTypeCaseAlreadyExist;
import com.codenbugs.ms_project.services.type_cases.TypeCasesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/type_cases")
@AllArgsConstructor
public class TypeCasesController {

    private final TypeCasesService typeCasesService;

    @GetMapping("")
    public ResponseEntity<List<TypeCasesResponse>> getAllCases() {
        List<TypeCasesResponse> typeCases = this.typeCasesService.getAllTypeCases();
        return ResponseEntity.ok(typeCases);
    }

    @PostMapping("")
    public ResponseEntity<TypeCasesResponse> saveNewCase(@RequestBody TypeCasesRequest typeCasesRequest) throws NameTypeCaseAlreadyExist {
        TypeCasesResponse newTypeCase = this.typeCasesService.create(typeCasesRequest);
        return ResponseEntity.ok(newTypeCase);
    }

}
