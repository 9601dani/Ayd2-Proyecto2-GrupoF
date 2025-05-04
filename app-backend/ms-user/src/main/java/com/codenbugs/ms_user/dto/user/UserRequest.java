package com.codenbugs.ms_user.dto.user;

import java.math.BigDecimal;

public record UserRequest(String username, String email, String firstName, String lastName, BigDecimal salaryPerHour, Integer role, String password ) {
}
