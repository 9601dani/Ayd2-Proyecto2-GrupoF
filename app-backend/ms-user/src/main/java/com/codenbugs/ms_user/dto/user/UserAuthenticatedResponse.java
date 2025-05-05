package com.codenbugs.ms_user.dto.user;

import com.codenbugs.ms_user.dto.token.TokenResponse;
import com.codenbugs.ms_user.models.user.User;

public record UserAuthenticatedResponse(Integer id, String username, String photo, TokenResponse token) {

    public UserAuthenticatedResponse(User user, TokenResponse token) {
        this(user.getId(), user.getUsername(), user.getPhoto(), token);
    }
}
