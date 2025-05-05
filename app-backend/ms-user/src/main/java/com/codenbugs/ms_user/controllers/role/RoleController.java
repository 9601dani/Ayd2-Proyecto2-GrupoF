package com.codenbugs.ms_user.controllers.role;

import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.services.role.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/roles")
@AllArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/all")
    public List<Role> getAllRoles() {
        return this.roleService.findAll();
    }
}
