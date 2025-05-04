package com.codenbugs.ms_user.models.user;

import com.codenbugs.ms_user.builders.user.ConcreteUserBuilder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal salaryPerHour;
    private String photo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isEnabled;

    @Column(name = "FK_Role")
    private Integer role;
    private String token;

    public User(ConcreteUserBuilder concreteUserBuilder) {
        this.id = concreteUserBuilder.getId();
        this.username = concreteUserBuilder.getUsername();
        this.password = concreteUserBuilder.getPassword();
        this.firstName = concreteUserBuilder.getFirstName();
        this.lastName = concreteUserBuilder.getLastName();
        this.email = concreteUserBuilder.getEmail();
        this.salaryPerHour = concreteUserBuilder.getSalaryPerHour();
        this.photo = concreteUserBuilder.getPhoto();
        this.role = concreteUserBuilder.getRole();
        this.token = concreteUserBuilder.getToken();
        this.createdAt = concreteUserBuilder.getCreatedAt();
        this.updatedAt = concreteUserBuilder.getUpdatedAt();
        this.isEnabled = concreteUserBuilder.getIsEnabled();
    }
}
