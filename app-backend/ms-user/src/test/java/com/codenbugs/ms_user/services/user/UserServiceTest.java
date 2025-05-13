package com.codenbugs.ms_user.services.user;

import com.codenbugs.ms_user.builders.user.ConcreteUserBuilder;
import com.codenbugs.ms_user.client.UploadRestClient;
import com.codenbugs.ms_user.dto.token.TokenResponse;
import com.codenbugs.ms_user.dto.user.*;
import com.codenbugs.ms_user.exceptions.user.RoleNotFoundException;
import com.codenbugs.ms_user.exceptions.user.UserAlreadyExist;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.exceptions.user.upload.NotCreatedException;
import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.models.user.User;
import com.codenbugs.ms_user.repository.UserRepository;
import com.codenbugs.ms_user.services.role.RoleService;
import com.codenbugs.ms_user.services.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private RoleService roleService;

    @Mock
    private UploadRestClient uploadRestClient;
    @InjectMocks
    private UserServiceImpl userService;

    private final Integer ID = 1;
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String ENCODED_PASSWORD = "encoded_password";
    private final String EMAIL = "email@example.com";
    private final String FIRSTNAME = "firstname";
    private final String LASTNAME = "lastname";
    private final Integer ROLE_ID = 1;
    private final String ROLE_NAME = "role_name";
    private final BigDecimal SALARY = new BigDecimal(500);
    private final String PHOTO = "photo";
    private final String PHOTO_VALUE = "photo_value";
    private final String INVALID_PASSWORD = "Invalid password";

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        LocalDateTime now = LocalDateTime.now();

        user = new ConcreteUserBuilder()
                .withId(ID)
                .withUsername(USERNAME)
                .withEmail(EMAIL)
                .withPassword(PASSWORD)
                .withFirstName(FIRSTNAME)
                .withLastName(LASTNAME)
                .withRole(ROLE_ID)
                .withIsEnabled(true)
                .withPhoto(PHOTO)
                .withCreatedAt(now)
                .withUpdatedAt(now)
                .build();

        role = new Role();
        role.setId(ROLE_ID);
        role.setName(ROLE_NAME);
    }


    @Test
    void findByIdValidIdReturnsUser() throws Exception {
        // Arrange
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        // Act
        UserResponse result = userService.findById(ID);

        // Assert
        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        verify(userRepository).findById(ID);
    }

    @Test
    void findByIdInvalidIdThrowsUserNotFoundException() {
        // Arrange
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findById(ID));
        verify(userRepository).findById(ID);
    }

    @Test
    void createUserAlreadyExistsThrowsUserAlreadyExist() {
        // Arrange
        UserRequest userRequest = new UserRequest(USERNAME, EMAIL, FIRSTNAME, LASTNAME, SALARY, ROLE_ID, PASSWORD);
        when(userRepository.findByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserAlreadyExist.class, () -> userService.create(userRequest));
        verify(userRepository).findByUsernameOrEmail(USERNAME, EMAIL);
    }

    @Test
    void updateValidUserReturnsUpdatedUser() throws Exception {
        // Arrange
        UserRequest request = new UserRequest(USERNAME, EMAIL, FIRSTNAME, LASTNAME, SALARY, ROLE_ID, PASSWORD);
        when(userRepository.findByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(Optional.of(user));
        when(roleService.findById(ROLE_ID)).thenReturn(role);

        // Act
        ListUserResponse response = userService.update(request);

        // Assert
        assertNotNull(response);
        assertEquals(USERNAME, response.username());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateInvalidUserThrowsUserNotFound() {
        // Arrange
        UserRequest request = new UserRequest(USERNAME, EMAIL, FIRSTNAME, LASTNAME, SALARY, ROLE_ID, PASSWORD);
        when(userRepository.findByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.update(request));
    }

    @Test
    void deleteValidUserDisablesUser() throws Exception {
        // Arrange
        user.setIsEnabled(true);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(roleService.findById(ROLE_ID)).thenReturn(role);

        // Act
        ListUserResponse result = userService.delete(USERNAME);

        // Assert
        assertFalse(result.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void enableValidUserEnablesUser() throws Exception {
        // Arrange
        user.setIsEnabled(false);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(roleService.findById(ROLE_ID)).thenReturn(role);

        // Act
        ListUserResponse result = userService.enable(USERNAME);

        // Assert
        assertTrue(result.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void authenticateValidUserReturnsToken() throws Exception {
        // Arrange
        UserAuthRequest authRequest = new UserAuthRequest(USERNAME, PASSWORD);
        user.setPassword(PASSWORD);
        user.setIsEnabled(true);
        when(userRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);
        TokenResponse tokenResponse = new TokenResponse("access", "refresh");
        when(tokenService.getTokens(user)).thenReturn(tokenResponse);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserAuthenticatedResponse result = userService.authenticate(authRequest);

        // Assert
        assertEquals(USERNAME, result.username());
        assertEquals("refresh", result.token().refreshToken());
    }

    @Test
    void logoutValidUserClearsToken() throws Exception {
        // Arrange
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        // Act
        userService.logout(ID);

        // Assert
        verify(userRepository).save(user);
        assertNull(user.getToken());
    }

    @Test
    void getUserByUsernameValidReturnsProfile() throws Exception {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(roleService.findById(ROLE_ID)).thenReturn(role);

        // Act
        UserMyProfileResponse response = userService.getUserByUsername(USERNAME);

        // Assert
        assertNotNull(response);
        assertEquals(USERNAME, response.username());
    }

    @Test
    void updateMyProfileValidUpdatesUser() throws Exception {
        // Arrange
        UserMyProfile request = new UserMyProfile(ID, PHOTO, USERNAME, EMAIL, true, FIRSTNAME, LASTNAME, PASSWORD);
        when(userRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserMyProfileResponse response = userService.updateMyProfile(request);

        // Assert
        assertNotNull(response);
        assertEquals(USERNAME, response.username());
    }

    @Test
    void findAllReturnsListUserResponses() throws Exception {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(roleService.findById(ROLE_ID)).thenReturn(role);

        // Act
        List<ListUserResponse> result = userService.findAll();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getUsersByRoleReturnsUserResponseWithNames() {
        // Arrange
        when(userRepository.findByRole(ROLE_ID)).thenReturn(List.of(user));

        // Act
        List<UserResponseWithName> result = userService.getUsersByRole(ROLE_ID);

        // Assert
        assertEquals(1, result.size());
        assertEquals(USERNAME, result.get(0).username());
    }

    @Test
    void updatePhotoPathUserUpdatesPhoto() throws Exception {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(uploadRestClient.uploadImage(mockFile))
                .thenReturn(new HashMap<>(Map.of("objectName", "newPhoto.jpg")));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals("newPhoto.jpg", savedUser.getPhoto());
            return savedUser;
        });

        // Act
        UserResponse response = userService.updatePhotoPathUser(ID, mockFile);

        // Assert
        assertEquals("newPhoto.jpg", response.photo());
    }

    @Test
    void updatePhotoPathUserThrowsNotCreatedExceptionWhenUploadFails() throws Exception {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        user.setPhoto("oldPhoto.jpg");
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(uploadRestClient.uploadImage(mockFile)).thenThrow(new NotCreatedException("Error uploading file"));

        // Act & Assert
        NotCreatedException exception = assertThrows(NotCreatedException.class, () ->
                userService.updatePhotoPathUser(ID, mockFile)
        );

        assertEquals("Error uploading file", exception.getMessage());
    }

    @Test
    void updatePhotoPathUserThrowsExceptionWhenObjectNameMissing() throws Exception {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(uploadRestClient.uploadImage(mockFile)).thenReturn(new HashMap<>());

        // Act & Assert
        NotCreatedException exception = assertThrows(NotCreatedException.class,
                () -> userService.updatePhotoPathUser(ID, mockFile));
        assertEquals("No se recibió objectName desde el upload", exception.getMessage());
    }


    @Test
    void authenticateUserDisabledThrowsException() {
        // Arrange
        UserAuthRequest authRequest = new UserAuthRequest(USERNAME, PASSWORD);
        user.setIsEnabled(false);
        when(userRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);

        // Act & Assert
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.authenticate(authRequest));
        assertEquals("User not enabled", ex.getMessage());
    }

    @Test
    void updateMyProfileIgnoresBlankPassword() throws Exception {
        // Arrange
        UserMyProfile request = new UserMyProfile(ID, PHOTO, USERNAME, EMAIL, true, FIRSTNAME, LASTNAME, "  "); // contraseña vacía
        when(userRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserMyProfileResponse response = userService.updateMyProfile(request);

        // Assert
        assertEquals(USERNAME, response.username());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findAllThrowsRuntimeExceptionWhenRoleNotFound() throws RoleNotFoundException {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(roleService.findById(ROLE_ID)).thenThrow(new RoleNotFoundException("Rol no encontrado"));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.findAll());
        assertTrue(ex.getMessage().contains("Rol no encontrado"));
    }


    @Test
    void createValidUserReturnsListUserResponse() throws Exception {
        // Arrange
        UserRequest request = new UserRequest(USERNAME, EMAIL, FIRSTNAME, LASTNAME, SALARY, ROLE_ID, PASSWORD);
        when(userRepository.findByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encodedPassword");
        when(roleService.findById(ROLE_ID)).thenReturn(role);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ListUserResponse response = userService.create(request);

        // Assert
        assertNotNull(response);
        assertEquals(USERNAME, response.username());
        verify(userRepository).save(any(User.class));
        verify(roleService).findById(ROLE_ID);
    }

    @Test
    void getUserByUsernameThrowsWhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () ->
                userService.getUserByUsername(USERNAME)
        );

        assertEquals("No se encontró el usuario", ex.getMessage());
    }

    @Test
    void findRoleOrThrowThrowsUserNotFoundExceptionWhenRoleMissing() throws Exception {
        // Arrange
        String errorMessage = "No se guardo, rol no existe";
        when(roleService.findById(ROLE_ID)).thenThrow(new RoleNotFoundException("Role not found"));

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            UserRequest request = new UserRequest(USERNAME, EMAIL, FIRSTNAME, LASTNAME, SALARY, ROLE_ID, PASSWORD);
            when(userRepository.findByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(Optional.empty());
            when(passwordEncoder.encode(PASSWORD)).thenReturn("encoded");

            userService.create(request);
        });

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void authenticateThrowsUserNotFoundWhenUserDoesNotExist() {
        // Arrange
        UserAuthRequest request = new UserAuthRequest(USERNAME, PASSWORD);
        when(userRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.authenticate(request));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void authenticateThrowsUserNotFoundWhenPasswordInvalid() {
        // Arrange
        UserAuthRequest request = new UserAuthRequest(USERNAME, PASSWORD);
        when(userRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(false);

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.authenticate(request));

        assertEquals(INVALID_PASSWORD, exception.getMessage());
    }

    @Test
    void updatePhotoPathUserThrowsWhenObjectNameIsNull() throws Exception {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put(PHOTO, PHOTO_VALUE);
        when(uploadRestClient.uploadImage(mockFile)).thenReturn((HashMap<String, String>) uploadResult);

        // Act & Assert
        NotCreatedException exception = assertThrows(NotCreatedException.class,
                () -> userService.updatePhotoPathUser(ID, mockFile));

        assertEquals("No se recibió objectName desde el upload", exception.getMessage());
    }

    @Test
    void enableThrowsUserNotFoundExceptionWhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.enable(USERNAME));

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void deleteThrowsUserNotFoundExceptionWhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.delete(USERNAME));

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void updatePhotoPathUserThrowsUserNotFoundExceptionWhenUserMissing() {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.updatePhotoPathUser(ID, mockFile)
        );

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void updateMyProfileUserThrowsUserNotFoundExceptionWhenUserMissing() {

        // Arrange
        UserMyProfile userMyProfile = new UserMyProfile(ID, PHOTO, USERNAME, EMAIL, true, FIRSTNAME, LASTNAME, PASSWORD);

        when(userRepository.findByUsernameOrEmail(userMyProfile.username(), userMyProfile.username()))
                .thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateMyProfile(userMyProfile);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void logoutThrowsUserNotFoundExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.logout(ID));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(ID);
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void updateMyProfileWithNullPasswordKeepsExistingPassword() throws Exception {
        // Arrange
        String nullPassword = null;
        UserMyProfile request = new UserMyProfile(ID, PHOTO, USERNAME, EMAIL, true, FIRSTNAME, LASTNAME, nullPassword);
        when(userRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals(PASSWORD, savedUser.getPassword());
            return savedUser;
        });

        // Act
        UserMyProfileResponse response = userService.updateMyProfile(request);

        // Assert
        assertEquals(USERNAME, response.username());
    }

    @Test
    void updateMyProfileWithSamePasswordKeepsHash() throws Exception {
        // Arrange
        UserMyProfile request = new UserMyProfile(ID, PHOTO, USERNAME, EMAIL, true, FIRSTNAME, LASTNAME, PASSWORD);
        when(userRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals(PASSWORD, savedUser.getPassword());
            return savedUser;
        });

        // Act
        UserMyProfileResponse response = userService.updateMyProfile(request);

        // Assert
        assertEquals(USERNAME, response.username());
    }

    @Test
    void updateMyProfileWithNewPasswordEncodesIt() throws Exception {
        // Arrange
        String newPassword = PASSWORD;
        String encoded = ENCODED_PASSWORD;
        UserMyProfile request = new UserMyProfile(user);

        when(userRepository.findByUsernameOrEmail(USERNAME, USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(newPassword, PASSWORD)).thenReturn(false);
        when(passwordEncoder.encode(newPassword)).thenReturn(encoded);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals(encoded, savedUser.getPassword());
            return savedUser;
        });

        // Act
        UserMyProfileResponse response = userService.updateMyProfile(request);

        // Assert
        assertEquals(USERNAME, response.username());
    }



}
