package com.codenbugs.ms_company.model.setting;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "key_value")
    private String keyValue;

    @Column(name = "value_type")
    private String valueType;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Column(name = "FK_Setting_Type")
    private Integer fkSettingType;

}
