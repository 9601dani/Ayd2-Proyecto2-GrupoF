package com.codenbugs.ms_project.model.cases;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "history_case_phases")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryCasePhase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FK_Case",nullable = false)
    private Integer fkCase;

    @Column(name = "FK_User",nullable = false)
    private Integer fkUser;

    @Column(name = "FK_Case_Phase",nullable = false)
    private Integer fkCasePhase;

    @Column(name = "is_completed",nullable = false)
    private Boolean isCompleted = Boolean.FALSE;

    @Column(name = "time_spent",nullable = false)
    private BigDecimal timeSpent = BigDecimal.ZERO;
}
