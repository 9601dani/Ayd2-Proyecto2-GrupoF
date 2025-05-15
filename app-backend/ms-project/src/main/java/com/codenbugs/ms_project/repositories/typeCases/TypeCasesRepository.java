package com.codenbugs.ms_project.repositories.typeCases;

import com.codenbugs.ms_project.model.cases.TypeCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeCasesRepository extends JpaRepository<TypeCase, Integer> {

    TypeCase findByName(String name);
}
