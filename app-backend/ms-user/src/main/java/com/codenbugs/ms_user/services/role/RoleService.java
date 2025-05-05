package com.codenbugs.ms_user.services.role;

import com.codenbugs.ms_user.exceptions.user.RoleNotFoundException;
import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.repository.RoleRepository;

import java.util.List;

public interface RoleService {
    Role findById(Integer id) throws RoleNotFoundException;
    List<Role> findAll();

}
