package com.codenbugs.ms_user.services.user;

import com.codenbugs.ms_user.builders.user.ConcreteUserBuilder;
import com.codenbugs.ms_user.dto.user.*;
import com.codenbugs.ms_user.exceptions.user.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = UserException.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RoleService roleService;

    @Override
    public UserResponse findById(Integer id) throws UserNotFoundException {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("No se encontró el usuario");
        }

        return new UserResponse(optionalUser.get());

    }

    @Override
    public ListUserResponse create(UserRequest user) throws UserAlreadyExist, UserNotFoundException {

        Optional<User> oldUser = userRepository.findByUsernameOrEmail(user.username(), user.email());

        if (oldUser.isPresent()) {
            throw new UserAlreadyExist(user.username()+" already exists");
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

        Role role = findRoleOrThrow(user.role(), "Not saved user");
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

        Role role = findRoleOrThrow(user.role(), "Not updated user");
        return new ListUserResponse(newUser, role);

    }


    @Override
    public ListUserResponse delete(String username) throws UserNotFoundException {
        User userToDelete = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        userToDelete.setIsEnabled(false);

        userRepository.save(userToDelete);
        Role role = findRoleOrThrow(userToDelete.getRole(), "Not updated user");
        return new ListUserResponse(userToDelete, role);


    }

    @Override
    public ListUserResponse enable(String username) throws UserNotFoundException {
        User userToDelete = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        userToDelete.setIsEnabled(true);

        userRepository.save(userToDelete);
        Role role = findRoleOrThrow(userToDelete.getRole(), "Not updated user");
        return new ListUserResponse(userToDelete, role);
    }

    @Override
    public UserAuthenticatedResponse authenticate(UserAuthRequest userAuthRequest) throws UserNotFoundException {
        String usernameOrEmail = userAuthRequest.usernameOrEmail();
        String password = userAuthRequest.password();

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new UserNotFoundException("User not found"));
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
        user = this.userRepository.save(user);

        return new UserAuthenticatedResponse(user, tokenResponse);
    }

    @Override
    public void logout(Integer id) throws UserNotFoundException {
        User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setToken(null);
        this.userRepository.save(user);
    }

    @Override
    public List<ListUserResponse> findAll() {
        return this.userRepository.findAll().stream()
                .map(user -> {
                        try {
                            Role role = this.roleService.findById(user.getRole());
                            return new ListUserResponse(user, role);
                        } catch (RoleException e) {
                            throw new RuntimeException("Role not found", e);
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



}
