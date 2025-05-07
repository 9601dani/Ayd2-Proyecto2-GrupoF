package com.codenbugs.ms_company.controllers.setting;

import com.codenbugs.ms_company.dtos.setting.SettingModule;
import com.codenbugs.ms_company.services.setting.SettingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
