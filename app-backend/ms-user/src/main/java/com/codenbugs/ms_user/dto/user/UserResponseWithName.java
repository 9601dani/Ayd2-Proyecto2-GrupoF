package com.codenbugs.ms_user.dto.user;

import com.codenbugs.ms_user.models.user.User;

public record UserResponseWithName(Integer id, String username, String firstName, String lastName) {

    public UserResponseWithName(User user) {
        this(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());
    }
}
