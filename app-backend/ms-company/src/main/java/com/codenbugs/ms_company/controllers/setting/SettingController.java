package com.codenbugs.ms_company.controllers.setting;

import com.codenbugs.ms_company.dtos.setting.SettingDto;
import com.codenbugs.ms_company.dtos.setting.SettingModule;
import com.codenbugs.ms_company.exceptions.feign.NotCreatedException;
import com.codenbugs.ms_company.exceptions.setting.SettingException;
import com.codenbugs.ms_company.exceptions.setting.SettingNotFoundException;
import com.codenbugs.ms_company.model.setting.Setting;
import com.codenbugs.ms_company.services.setting.SettingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/settings")
@AllArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @GetMapping()
    public ResponseEntity<List<SettingModule>> findAll() {
        List<SettingModule> settingModules = settingService.findAll();
        return ResponseEntity.ok(settingModules);
    }

    @GetMapping("/find-by-key-names")
    public ResponseEntity<List<SettingDto>> findAllByKeyName(@RequestParam("keyName") List<String> keyNames) throws SettingNotFoundException {
        List<SettingDto> settingDtos = this.settingService.findAllByKeyName(keyNames);
        return ResponseEntity.ok(settingDtos);
    }

    @PutMapping()
    public ResponseEntity<List<SettingDto>> updateSettings(
            @RequestParam Map<String, String> formFields,
            @RequestParam Map<String, MultipartFile> files
    ) throws SettingException, NotCreatedException {
        List<SettingDto> settings = this.settingService.update(formFields, files);
        return ResponseEntity.ok(settings);
    }
}
