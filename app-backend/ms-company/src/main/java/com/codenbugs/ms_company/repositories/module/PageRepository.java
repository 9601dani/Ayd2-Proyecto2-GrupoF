package com.codenbugs.ms_company.repositories.module;

import com.codenbugs.ms_company.dtos.module.ModuleResponseDto;
import com.codenbugs.ms_company.model.module.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface PageRepository extends JpaRepository<Page, Integer> {
    @Query(value = """
        SELECT new com.codenbugs.ms_company.dtos.module.ModuleResponseDto(m.id ,m.name, m.path,p.id, p.name, p.path)
        FROM Page p
        LEFT JOIN Module m ON p.fkModule = m.id
        LEFT JOIN RoleHasPage rhp ON p.id = rhp.fkPage
        WHERE rhp.fkRole = :roleId
    """)
    List<ModuleResponseDto> findPagesByRoleId(@Param("roleId") Integer roleId);
}
