package com.codenbugs.ms_company.model.module;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="modules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String path;

    @Column(name = "is_enabled",columnDefinition = "TINYINT")
    private Boolean isEnabled;
}
