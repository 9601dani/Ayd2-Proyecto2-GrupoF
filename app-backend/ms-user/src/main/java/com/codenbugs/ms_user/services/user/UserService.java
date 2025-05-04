package com.codenbugs.ms_user.services.user;

import com.codenbugs.ms_user.dto.user.*;
import com.codenbugs.ms_user.exceptions.user.UserAlreadyExist;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserResponse findById(Integer id) throws UserNotFoundException;
    ListUserResponse create(UserRequest user) throws UserAlreadyExist, UserNotFoundException;
    ListUserResponse update(UserRequest user) throws UserNotFoundException;
    ListUserResponse delete(String username) throws UserNotFoundException;
    ListUserResponse enable(String username) throws UserNotFoundException;
    List<ListUserResponse> findAll();
    UserAuthenticatedResponse authenticate(UserAuthRequest userAuthRequest) throws UserNotFoundException;
    void logout(Integer id) throws UserNotFoundException;
}
