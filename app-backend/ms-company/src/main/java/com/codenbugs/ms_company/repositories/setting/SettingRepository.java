package com.codenbugs.ms_company.repositories.setting;

import com.codenbugs.ms_company.dtos.setting.SettingDvo;
import com.codenbugs.ms_company.exceptions.setting.SettingNotFoundException;
import com.codenbugs.ms_company.model.setting.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Integer> {

    @Query(value = """
    SELECT 
            s.id,
            s.key_name AS keyName,
            s.key_value AS keyValue,
            s.value_type AS valueType,
            s.label_value AS labelValue,
            s.is_enabled AS isEnabled,
            s.FK_Setting_Type fkSettingType,
            st.name AS settingTypeName
    FROM settings s
    LEFT JOIN setting_types st ON st.id = s.FK_Setting_Type
    WHERE s.is_enabled = true
    """, nativeQuery = true)
    List<SettingDvo> findAllByIsEnabled();

    Optional<Setting> findByKeyName(String keyName) throws SettingNotFoundException;
}
