package com.codenbugs.ms_user.builders.user;

import com.codenbugs.ms_user.models.user.User;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ConcreteUserBuilder implements UserBuilder {

    private Integer id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal salaryPerHour = BigDecimal.ZERO;
    private String photo;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private Boolean isEnabled = true;
    private Integer role;
    private String token;

    @Override
    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public UserBuilder withSalary(BigDecimal salary) {
        this.salaryPerHour = salary;
        return this;
    }

    @Override
    public UserBuilder withRole(Integer role) {
        this.role = role;
        return this;
    }

    @Override
    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Override
    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder withIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public UserBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public UserBuilder withPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public UserBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserBuilder withUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public UserBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public User build() {
        return new User(this);
    }
}
