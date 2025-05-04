package com.codenbugs.ms_user.builders.user;

import java.math.BigDecimal;

public interface UserBuilder {

    UserBuilder withUsername(String username);
    UserBuilder withPassword(String password);
    UserBuilder withEmail(String email);
    UserBuilder withSalary(BigDecimal salary);
    UserBuilder withRole(Integer role);
    UserBuilder withFirstName(String firstName);
    UserBuilder withLastName(String lastName);
}
