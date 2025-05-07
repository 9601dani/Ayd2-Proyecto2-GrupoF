package com.codenbugs.ms_user.dto.user;

import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.models.user.User;

import java.math.BigDecimal;

public record UserMyProfile(Integer id, String photo, String username, String email, boolean isEnabled, String firstName, String lastName, String password) {
    public UserMyProfile (User user) {
        this(user.getId(), user.getPhoto(), user.getUsername(), user.getEmail(), user.getIsEnabled(), user.getFirstName(), user.getLastName(), user.getPassword());
    }
}
