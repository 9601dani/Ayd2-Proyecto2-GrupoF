package com.codenbugs.ms_company.model.setting;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "setting_types")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "name")
public class SettingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

}
