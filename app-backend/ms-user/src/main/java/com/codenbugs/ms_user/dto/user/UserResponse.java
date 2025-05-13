package com.codenbugs.ms_user.dto.user;

import com.codenbugs.ms_user.models.user.User;

import java.math.BigDecimal;

public record UserResponse(Integer id, String username, Integer role, String photo, BigDecimal salaryPerHour, Boolean isEnabled) {

    public UserResponse(User user) {
        this(user.getId(), user.getUsername(), user.getRole(), user.getPhoto(), user.getSalaryPerHour(), user.getIsEnabled());
    }
}
