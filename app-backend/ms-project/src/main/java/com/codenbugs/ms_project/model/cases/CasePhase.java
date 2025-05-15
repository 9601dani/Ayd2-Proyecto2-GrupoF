package com.codenbugs.ms_project.model.cases;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "case_phases")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CasePhase {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name="FK_Case_Type")
    private Integer fkCaseType;

    @Column(name="next_phase")
    private Integer nextPhase;



}
