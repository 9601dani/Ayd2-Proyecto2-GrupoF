package com.codenbugs.ms_company.model.module;

import jakarta.persistence.*;

@Entity
@Table(name ="pages")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String path;

    @Column(name = "FK_Module")
    private Integer fkModule;

    @Column(name = "is_enabled",columnDefinition = "TINYINT")
    private Boolean isEnabled;
}
