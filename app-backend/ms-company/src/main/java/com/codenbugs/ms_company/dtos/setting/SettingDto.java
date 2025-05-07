package com.codenbugs.ms_company.dtos.setting;

public record SettingDto(String keyName, String keyValue, String valueType) {

    public SettingDto(SettingDvo settingDvo) {
        this(settingDvo.keyName(), settingDvo.keyValue(), settingDvo.valueType());
    }
}
