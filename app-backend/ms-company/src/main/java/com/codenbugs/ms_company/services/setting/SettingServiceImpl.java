package com.codenbugs.ms_company.services.setting;

import com.codenbugs.ms_company.dtos.setting.SettingDto;
import com.codenbugs.ms_company.dtos.setting.SettingDvo;
import com.codenbugs.ms_company.dtos.setting.SettingModule;
import com.codenbugs.ms_company.exceptions.feign.NotCreatedException;
import com.codenbugs.ms_company.exceptions.setting.SettingException;
import com.codenbugs.ms_company.exceptions.setting.SettingNotFoundException;
import com.codenbugs.ms_company.exceptions.setting.SettingNotUpdatedException;
import com.codenbugs.ms_company.model.setting.Setting;
import com.codenbugs.ms_company.repositories.setting.SettingRepository;
import com.codenbugs.ms_company.utils.UploadFileComponent;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = {SettingException.class, NotCreatedException.class})
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;
    private final UploadFileComponent uploadFileComponent;

    @Override
    public List<SettingModule> findAll() {
        List<SettingDvo> settingDvos = settingRepository.findAllByIsEnabled();
        List<String> distinctSettingTypeNames = settingDvos.stream().map(SettingDvo::settingTypeName).distinct().toList();

        List<SettingModule> settingModules = new ArrayList<>();
        for(String settingTypeName : distinctSettingTypeNames) {
            List<SettingDto> settingDtos = settingDvos.stream()
                    .filter(s -> s.settingTypeName().equals(settingTypeName))
                    .map(SettingDto::new)
                    .toList();
            settingModules.add(new SettingModule(settingTypeName, settingDtos));
        }

        return settingModules;
    }

    @Override
    public Setting findByKeyName(String keyName) throws SettingNotFoundException {
        return this.settingRepository.findByKeyName(keyName)
                .orElseThrow(() -> new SettingNotFoundException("No se encontró la configuración."));
    }

    @Override
    public List<SettingDto> update(Map<String, String> settings, Map<String, MultipartFile> files) throws SettingException, NotCreatedException {
        List<Setting> settingsUpdated = new ArrayList<>();
        for(Map.Entry<String, String> entry : settings.entrySet()) {
            Setting setting = findByKeyName(entry.getKey());
            if(!setting.getIsEnabled()) {
                throw new SettingNotUpdatedException("La configuración no se encuentra activa");
            }
            setting.setKeyValue(entry.getValue());
            Setting updated =this.settingRepository.save(setting);
            settingsUpdated.add(updated);

        }

        for(Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            Setting setting = findByKeyName(entry.getKey());
            if(!setting.getIsEnabled()) {
                throw new SettingNotUpdatedException("La configuración no se encuentra activa");
            }

            String fileName = this.uploadFileComponent.uploadFile(entry.getValue());
            setting.setKeyValue(fileName);
            Setting updated = this.settingRepository.save(setting);
            settingsUpdated.add(updated);
        }
        return settingsUpdated.stream().map(SettingDto::new).toList();
    }

    @Override
    public List<SettingDto> findAllByKeyName(List<String> keyNames) throws SettingNotFoundException {
        List<Setting> settings = new ArrayList<>();
        for(String keyName : keyNames) {
            Setting setting = this.findByKeyName(keyName);
            settings.add(setting);
        }

        return settings.stream().map(SettingDto::new).toList();
    }
}

