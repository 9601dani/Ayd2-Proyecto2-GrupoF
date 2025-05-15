package com.codenbugs.ms_report.dtos.user;


import java.math.BigDecimal;

public record UserResponse(Integer id, String username, Integer role, String photo, BigDecimal salaryPerHour) {
}
