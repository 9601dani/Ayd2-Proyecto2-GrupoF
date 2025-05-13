package com.codenbugs.ms_user.services.role;

import com.codenbugs.ms_user.exceptions.user.RoleNotFoundException;
import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private final String ROLENAME = "rolename";
    private final Integer ID = 1;

    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setId(ID);
        role.setName(ROLENAME);
    }

    @Test
    void findByIdvalid() throws Exception {
        // Arrange
        when(roleRepository.findById(ID)).thenReturn(role);

        // Act
        Role result = roleService.findById(ID);

        // Assert
        assertNotNull(result);
        assertEquals(ROLENAME, result.getName());
        verify(roleRepository).findById(ID);
    }

    @Test
    void findByIdNullId() {
        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> roleService.findById(null));
        verify(roleRepository, never()).findById((Integer) any());
    }

    @Test
    void findByIdNotExistentIdThrowsRoleNotFoundException() {
        // Arrange
        when(roleRepository.findById(ID)).thenReturn(null);

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> roleService.findById(ID));
        verify(roleRepository).findById(ID);
    }

    @Test
    void findAllReturnsRoleList() {
        // Arrange
        Role nRole = new Role();
        role.setId(ID+1);
        role.setName(ROLENAME);
        List<Role> roles = List.of(role,nRole);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        List<Role> result = roleService.findAll();

        // Assert
        assertEquals(ID+1, result.size());
        verify(roleRepository).findAll();
    }

}
