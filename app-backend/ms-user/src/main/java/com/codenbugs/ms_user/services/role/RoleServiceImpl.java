package com.codenbugs.ms_user.services.role;

import com.codenbugs.ms_user.exceptions.user.RoleException;
import com.codenbugs.ms_user.exceptions.user.RoleNotFoundException;
import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    @Override
    public Role findById(Integer id) throws RoleNotFoundException {
        if(id == null){
            throw  new RoleNotFoundException("Rol no encontrado");
        }

        Role role = roleRepository.findById(id);

        if(role == null){
            throw  new RoleNotFoundException("Role no encontrado");
        }
        return role;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
