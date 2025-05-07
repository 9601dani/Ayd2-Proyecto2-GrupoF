package com.codenbugs.ms_company.services.setting;

import com.codenbugs.ms_company.dtos.setting.SettingDto;
import com.codenbugs.ms_company.dtos.setting.SettingDvo;
import com.codenbugs.ms_company.dtos.setting.SettingModule;
import com.codenbugs.ms_company.exceptions.setting.SettingException;
import com.codenbugs.ms_company.model.setting.Setting;
import com.codenbugs.ms_company.repositories.setting.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = SettingException.class)
@AllArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

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
    public Setting findById(int id) {
        return null;
    }

    @Override
    public List<Setting> update(List<Setting> settings) throws SettingException {
        return List.of();
    }
}
