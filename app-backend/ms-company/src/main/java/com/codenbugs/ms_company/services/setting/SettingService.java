package com.codenbugs.ms_company.services.setting;

import com.codenbugs.ms_company.dtos.setting.SettingDto;
import com.codenbugs.ms_company.dtos.setting.SettingModule;
import com.codenbugs.ms_company.exceptions.feign.NotCreatedException;
import com.codenbugs.ms_company.exceptions.setting.SettingException;
import com.codenbugs.ms_company.exceptions.setting.SettingNotFoundException;
import com.codenbugs.ms_company.model.setting.Setting;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface SettingService {

    List<SettingModule> findAll();
    Setting findByKeyName(String keyName) throws SettingNotFoundException;
    List<SettingDto> update(Map<String, String> settings, Map<String, MultipartFile> files) throws SettingException, NotCreatedException;
    List<SettingDto> findAllByKeyName(List<String> keyNames) throws SettingNotFoundException;

}
