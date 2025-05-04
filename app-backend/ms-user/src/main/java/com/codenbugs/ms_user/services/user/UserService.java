package com.codenbugs.ms_user.services.user;

import com.codenbugs.ms_user.dto.user.UserAuthRequest;
import com.codenbugs.ms_user.dto.user.UserAuthenticatedResponse;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.models.user.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findById(Integer id);
    User create(User user);
    User update(Integer id, User user);
    void delete(Integer id);
    UserAuthenticatedResponse authenticate(UserAuthRequest userAuthRequest) throws UserNotFoundException;

}
