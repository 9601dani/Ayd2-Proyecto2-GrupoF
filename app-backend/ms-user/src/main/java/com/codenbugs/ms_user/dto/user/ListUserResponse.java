package com.codenbugs.ms_user.dto.user;

import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.models.user.User;

import java.math.BigDecimal;

public record ListUserResponse(Integer id, String photo, String username, String email, BigDecimal salaryPerHour, boolean isEnabled, Role role, String firstName, String lastName) {
    public ListUserResponse(User user, Role role) {
        this(user.getId(), user.getPhoto(), user.getUsername(), user.getEmail(), user.getSalaryPerHour(), user.getIsEnabled(), role, user.getFirstName(), user.getLastName());
    }

}
