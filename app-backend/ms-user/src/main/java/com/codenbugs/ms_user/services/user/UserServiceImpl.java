package com.codenbugs.ms_user.services.user;

import com.codenbugs.ms_user.dto.user.TokenResponse;
import com.codenbugs.ms_user.dto.user.UserAuthRequest;
import com.codenbugs.ms_user.dto.user.UserAuthenticatedResponse;
import com.codenbugs.ms_user.exceptions.user.UserException;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.models.user.User;
import com.codenbugs.ms_user.repository.UserRepository;
import com.codenbugs.ms_user.services.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = UserException.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public User findById(Integer id) {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(Integer id, User user) {
        return null;
    }

    @Override
    public void delete(Integer id) {

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

        TokenResponse tokenResponse = this.tokenService.getTokens(user);
        user.setToken(tokenResponse.refreshToken());
        user = this.userRepository.save(user);

        return new UserAuthenticatedResponse(user.getUsername(), tokenResponse);
    }
}
