package com.codenbugs.ms_project.model.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @Column(name = "FK_User")
    private Integer fkUser;

    @Column(name = "FK_Case")
    private Integer fkCase;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "id_parent")
    private Integer idParent;

}
