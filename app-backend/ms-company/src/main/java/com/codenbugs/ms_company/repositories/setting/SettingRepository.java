package com.codenbugs.ms_company.repositories.setting;

import com.codenbugs.ms_company.dtos.setting.SettingDvo;
import com.codenbugs.ms_company.model.setting.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, Integer> {

    @Query(value = """
    SELECT s, st.name AS settingTypeName
    FROM Setting s
    LEFT JOIN SettingType st ON st.id = s.fkSettingType
    WHERE s.isEnabled = true
    """)
    List<SettingDvo> findAllByIsEnabled();
}
