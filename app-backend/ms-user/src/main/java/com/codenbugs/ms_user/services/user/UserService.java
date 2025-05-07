package com.codenbugs.ms_user.services.user;

import com.codenbugs.ms_user.dto.user.*;
import com.codenbugs.ms_user.exceptions.user.UserAlreadyExist;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.exceptions.user.upload.NotCreatedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    UserMyProfileResponse getUserByUsername(String username) throws UserNotFoundException;
    UserMyProfileResponse updateMyProfile(UserMyProfile userMyProfile) throws UserNotFoundException;

    UserResponse updatePhotoPathUser(Integer fkUser, MultipartFile file) throws UserNotFoundException, NotCreatedException;
}
