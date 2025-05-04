package com.codenbugs.ms_company.model.module;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="roles_has_pages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleHasPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FK_Role")
    private Integer fkRole;

    @Column(name = "FK_Page")
    private Integer fkPage;
}
