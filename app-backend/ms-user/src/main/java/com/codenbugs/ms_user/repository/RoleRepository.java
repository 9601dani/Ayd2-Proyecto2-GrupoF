package com.codenbugs.ms_user.repository;

import com.codenbugs.ms_user.models.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(Integer id);
}
