package com.codenbugs.ms_report.dtos.user;


import java.math.BigDecimal;

public record UserResponseWithName(Integer id, String username, String email, BigDecimal salaryPerHour, Boolean isEnabled, String firstName, String lastName) {

}
