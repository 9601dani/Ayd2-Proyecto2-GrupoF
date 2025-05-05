package com.codenbugs.ms_user.builders.user;

import com.codenbugs.ms_user.models.user.User;

import java.math.BigDecimal;

public interface UserBuilder<T> {

    T withUsername(String username);
    T withPassword(String password);
    T withEmail(String email);
    T withSalary(BigDecimal salary);
    T withRole(Integer role);
    T withFirstName(String firstName);
    T withLastName(String lastName);
}
