package com.codenbugs.ms_project.model.cases;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cases")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FK_Project")
    private Integer fkProject;

    @Column(name = "progress_percentage")
    private BigDecimal progressPercentage;

    private Integer FK_Case_Type;

    @Column(name = "limit_date")
    private LocalDateTime  limitDate;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    private String name;

    private String description;

    @Column(name = "is_cancelled")
    private Boolean isCancelled = false;

    @Column(name = "reason_cancellation")
    private String reasonCancellation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
