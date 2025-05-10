package com.codenbugs.ms_company.dtos.setting;

import com.codenbugs.ms_company.model.setting.Setting;

public record SettingDto(String keyName, String keyValue, String labelValue, String valueType) {

    public SettingDto(SettingDvo settingDvo) {
        this(settingDvo.keyName(), settingDvo.keyValue(), settingDvo.labelValue(), settingDvo.valueType());
    }

    public SettingDto(Setting setting) {
        this(setting.getKeyName(), setting.getKeyValue(), setting.getLabelValue(), setting.getValueType());
    }
}
