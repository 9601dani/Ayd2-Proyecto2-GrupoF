package com.codenbugs.ms_user.dto.user;

import com.codenbugs.ms_user.models.user.User;

public record UserMyProfileResponse(Integer id, String photo, String username, String email, boolean isEnabled, String firstName, String lastName) {
    public UserMyProfileResponse(User user){
        this(user.getId(), user.getPhoto(), user.getUsername(), user.getEmail(), user.getIsEnabled(), user.getFirstName(), user.getLastName());
    }
}
