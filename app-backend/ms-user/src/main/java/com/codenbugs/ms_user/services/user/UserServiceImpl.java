package com.codenbugs.ms_user.services.user;

import com.codenbugs.ms_user.builders.user.ConcreteUserBuilder;
import com.codenbugs.ms_user.client.UploadRestClient;
import com.codenbugs.ms_user.dto.user.*;
import com.codenbugs.ms_user.exceptions.user.*;
import com.codenbugs.ms_user.exceptions.user.upload.NotCreatedException;
import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.dto.token.TokenResponse;
import com.codenbugs.ms_user.dto.user.UserAuthRequest;
import com.codenbugs.ms_user.dto.user.UserAuthenticatedResponse;
import com.codenbugs.ms_user.dto.user.UserResponse;
import com.codenbugs.ms_user.exceptions.user.UserException;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.models.user.User;
import com.codenbugs.ms_user.repository.UserRepository;
import com.codenbugs.ms_user.services.role.RoleService;
import com.codenbugs.ms_user.services.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
@Setter
@Transactional(rollbackOn = UserException.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RoleService roleService;

    private final UploadRestClient uploadRestClient;

    @Override
    public UserResponse findById(Integer id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No se encontró el usuario"));
        return new UserResponse(user);

    }

    @Override
    public ListUserResponse create(UserRequest user) throws UserAlreadyExist, UserNotFoundException {

        Optional<User> oldUser = userRepository.findByUsernameOrEmail(user.username(), user.email());

        if (oldUser.isPresent()) {
            throw new UserAlreadyExist(user.username()+" Ya existe el usuario");
        }

        User newUser = new ConcreteUserBuilder()
                .withUsername(user.username())
                .withEmail(user.email())
                .withFirstName(user.firstName())
                .withLastName(user.lastName())
                .withSalary(user.salaryPerHour())
                .withRole(user.role())
                .withPassword(passwordEncoder.encode(user.password()))
                .build();

        userRepository.save(newUser);

        Role role = findRoleOrThrow(user.role(), "No se guardo, rol no existe");
        return new ListUserResponse(newUser, role);


    }

    @Override
    public ListUserResponse update(UserRequest user) throws UserNotFoundException {
        User userToUpdate = userRepository.findByUsernameOrEmail(user.username(),user.email()).orElseThrow(() -> new UserNotFoundException("User not found"));

        User newUser = new ConcreteUserBuilder()
                .withId(userToUpdate.getId())
                .withUsername(userToUpdate.getUsername())
                .withEmail(userToUpdate.getEmail())
                .withFirstName(user.firstName())
                .withLastName(user.lastName())
                .withSalary(user.salaryPerHour())
                .withRole(user.role())
                .withPassword(userToUpdate.getPassword())
                .withCreatedAt(userToUpdate.getCreatedAt())
                .withPhoto(userToUpdate.getPhoto())
                .withIsEnabled(userToUpdate.getIsEnabled())
                .build();

        userRepository.save(newUser);

        Role role = findRoleOrThrow(user.role(), "No se puedo actualizar el usuario, derivado del rol");
        return new ListUserResponse(newUser, role);

    }


    @Override
    public ListUserResponse delete(String username) throws UserNotFoundException {
        User userToDelete = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        userToDelete.setIsEnabled(false);

        userRepository.save(userToDelete);
        Role role = findRoleOrThrow(userToDelete.getRole(), "No existe el Rol");
        return new ListUserResponse(userToDelete, role);


    }

    @Override
    public ListUserResponse enable(String username) throws UserNotFoundException {
        User userToDelete = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        userToDelete.setIsEnabled(true);

        userRepository.save(userToDelete);
        Role role = findRoleOrThrow(userToDelete.getRole(), "No existe el Rol");
        return new ListUserResponse(userToDelete, role);
    }

    @Override
    public UserAuthenticatedResponse authenticate(UserAuthRequest userAuthRequest) throws UserNotFoundException {
        String usernameOrEmail = userAuthRequest.usernameOrEmail();
        String password = userAuthRequest.password();

        System.out.println("REQUEST: " + userAuthRequest);
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new UserNotFoundException("User not found"));
        System.out.println("USER: " + user.getUsername());
        String userPassword = user.getPassword();

        boolean isValidPassword = passwordEncoder.matches(password, userPassword);

        if (!isValidPassword) {
            throw new UserNotFoundException("Invalid password");
        }

        if(!user.getIsEnabled()) {
            throw new UserNotFoundException("User not enabled");
        }

        TokenResponse tokenResponse = this.tokenService.getTokens(user);
        user.setToken(tokenResponse.refreshToken());
        System.out.println("USER SAVED: " + user.getIsEnabled());
        user = this.userRepository.save(user);

        System.out.println("RETURN " + user.getToken());
        return new UserAuthenticatedResponse(user, tokenResponse);
    }

    @Override
    public void logout(Integer id) throws UserNotFoundException {
        User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setToken(null);
        this.userRepository.save(user);
    }

    @Override
    public UserMyProfileResponse getUserByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("No se encontró el usuario");
        }

        User user = optionalUser.get();
        User userToResponse = new ConcreteUserBuilder()
                .withId(user.getId())
                .withUsername(user.getUsername())
                .withEmail(user.getEmail())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withSalary(user.getSalaryPerHour())
                .withRole(user.getRole())
                .withPassword(user.getPassword())
                .withCreatedAt(user.getCreatedAt())
                .withPhoto(user.getPhoto())
                .withIsEnabled(user.getIsEnabled())
                .build();

        Role role = findRoleOrThrow(user.getRole(), "El usuario no cuenta con un rol");
        return new UserMyProfileResponse(userToResponse);
    }

    @Override
    public UserMyProfileResponse updateMyProfile(UserMyProfile userMyProfile) throws UserNotFoundException {
        User userToUpdate = userRepository.findByUsernameOrEmail(userMyProfile.username(), userMyProfile.username()).orElseThrow(() -> new UserNotFoundException("User not found"));
        updatePasswordIfNeeded(userToUpdate, userMyProfile.password());

        User newUser = new ConcreteUserBuilder()
                .withId(userToUpdate.getId())
                .withUsername(userToUpdate.getUsername())
                .withEmail(userMyProfile.email())
                .withFirstName(userMyProfile.firstName())
                .withLastName(userMyProfile.lastName())
                .withSalary(userToUpdate.getSalaryPerHour())
                .withRole(userToUpdate.getRole())
                .withPassword(userToUpdate.getPassword())
                .withCreatedAt(userToUpdate.getCreatedAt())
                .withPhoto(userToUpdate.getPhoto())
                .withIsEnabled(userToUpdate.getIsEnabled())
                .build();

        userRepository.save(newUser);

        return new UserMyProfileResponse(newUser);
    }

    @Override
    public UserResponse updatePhotoPathUser(Integer fkUser, MultipartFile file) throws UserNotFoundException, NotCreatedException {
        User user = this.userRepository.findById(fkUser).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        Map<String,String> result = this.uploadRestClient.uploadImage(file);
        String fileName = result.get("objectName");

        if (fileName == null) {
            throw new NotCreatedException("No se recibió objectName desde el upload");
        }

        HashMap<String, String> response = new HashMap<>();
        user.setPhoto(fileName);
        User userSaved = this.userRepository.save(user);

        return new UserResponse(userSaved);
    }

    @Override
    public List<ListUserResponse> findAll() {
        return this.userRepository.findAll().stream()
                .map(user -> {
                        try {
                            Role role = this.roleService.findById(user.getRole());
                            return new ListUserResponse(user, role);
                        } catch (RoleException e) {
                            throw new RuntimeException("Rol no encontrado", e);
                        }
                    })
                .toList();
    }

    private Role findRoleOrThrow(Integer roleId, String errorMessage) throws UserNotFoundException {
        try {
            return roleService.findById(roleId);
        } catch (RoleNotFoundException ex) {
            throw new UserNotFoundException(errorMessage);
        }
    }

    @Override
    public List<UserResponseWithName> getUsersByRole(Integer role) {
        return this.userRepository.findByRole(role).stream().map(UserResponseWithName::new).collect(Collectors.toList());
    }

    private void updatePasswordIfNeeded(User userToUpdate, String incomingPassword) {
        String currentPasswordHash = userToUpdate.getPassword();
        if (isValidPassword(incomingPassword)) {
            if (isPasswordChanged(incomingPassword, currentPasswordHash)) {
                userToUpdate.setPassword(encodePassword(incomingPassword));
            }
        } else {
            userToUpdate.setPassword(currentPasswordHash);
        }
    }

    private boolean isValidPassword(String password) {
        return password != null && !password.isBlank();
    }

    private boolean isPasswordChanged(String rawPassword, String encodedPassword) {
        return !passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
