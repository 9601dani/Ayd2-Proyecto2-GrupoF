package com.codenbugs.ms_user.controllers.role;

import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.services.role.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

@WebMvcTest(controllers = RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    private Role roleAdmin;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        roleAdmin = new Role();
        roleAdmin.setId(1);
        roleAdmin.setName("ADMIN");

        roleUser = new Role();
        roleUser.setId(2);
        roleUser.setName("USER");
    }

    @Test
    void shouldReturnAllRoles() throws Exception {
        // Arrange
        List<Role> roles = List.of(roleAdmin, roleUser);
        when(roleService.findAll()).thenReturn(roles);

        // Act & Assert
        mockMvc.perform(get("/v1/roles/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(roles)));
    }
}
