package com.codenbugs.ms_user.dto.user;

import com.codenbugs.ms_user.models.user.User;

import java.math.BigDecimal;

public record UserResponseWithName(Integer id, String username, String email, BigDecimal salaryPerHour, Boolean isEnabled, String firstName, String lastName) {

    public UserResponseWithName(User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getSalaryPerHour(), user.getIsEnabled(), user.getFirstName(), user.getLastName());
    }
}
