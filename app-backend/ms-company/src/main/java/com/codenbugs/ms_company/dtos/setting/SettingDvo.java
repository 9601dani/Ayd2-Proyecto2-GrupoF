package com.codenbugs.ms_company.dtos.setting;

public record SettingDvo(Integer id, String keyName, String keyValue, String valueType, Boolean isEnabled, Integer fkSettingType, String settingTypeName) {
}
