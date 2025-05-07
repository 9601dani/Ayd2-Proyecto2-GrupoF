package com.codenbugs.ms_company.services.setting;

import com.codenbugs.ms_company.dtos.setting.SettingModule;
import com.codenbugs.ms_company.exceptions.setting.SettingException;
import com.codenbugs.ms_company.model.setting.Setting;

import java.util.List;

public interface SettingService {

    List<SettingModule> findAll();
    Setting findById(int id);
    List<Setting> update(List<Setting> settings) throws SettingException;

}
