package com.codenbugs.ms_company.controllers.setting;

import com.codenbugs.ms_company.dtos.setting.SettingDto;
import com.codenbugs.ms_company.dtos.setting.SettingModule;
import com.codenbugs.ms_company.exceptions.feign.NotCreatedException;
import com.codenbugs.ms_company.exceptions.setting.SettingException;
import com.codenbugs.ms_company.exceptions.setting.SettingNotFoundException;
import com.codenbugs.ms_company.model.setting.Setting;
import com.codenbugs.ms_company.services.setting.SettingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SettingControllerTest {
    private final String SETTINGNAME = "settingName";
    private final Integer ID = 1;
    private final String KEYNAME = "keyName";
    private final String KEYVALUE = "keyValue";
    private final String VALUETYPE = "valueType";
    private final Boolean ENABLED = true;
    private final String LABELVALUE = "labelValue";
    private final Integer FKSETTINGTYPE = 1;
    private final Integer STATUSOK = 200;

    @Mock
    private SettingService settingService;

    @InjectMocks
    private SettingController settingController;

    private SettingDto settingDto;
    private SettingModule settingModule;
    private Setting setting;
    @BeforeEach
    void setUp() {
        setting = new Setting();
        setting.setId(ID);
        setting.setKeyName(KEYNAME);
        setting.setKeyValue(KEYVALUE);
        setting.setValueType(VALUETYPE);
        setting.setIsEnabled(ENABLED);
        setting.setLabelValue(LABELVALUE);
        setting.setFkSettingType(FKSETTINGTYPE);
        settingDto = new SettingDto(setting);


        settingModule = new SettingModule(SETTINGNAME, List.of(settingDto));
    }

    @Test
    void testFindAll() {
        // Arrange
        when(settingService.findAll()).thenReturn(List.of(settingModule));

        // Act
        ResponseEntity<List<SettingModule>> response = settingController.findAll();

        // Assert
        assertEquals(STATUSOK, response.getStatusCodeValue());
        assertEquals(ID, response.getBody().size());
        assertEquals(SETTINGNAME, response.getBody().get(0).settingName());
    }

    @Test
    void testFindAllByKeyName() throws SettingNotFoundException {
        // Arrange
        when(settingService.findAllByKeyName(List.of(KEYNAME))).thenReturn(List.of(settingDto));

        // Act
        ResponseEntity<List<SettingDto>> response = settingController.findAllByKeyName(List.of(KEYNAME));

        // Assert
        assertEquals(STATUSOK, response.getStatusCodeValue());
        assertEquals(ID, response.getBody().size());
        assertEquals(KEYNAME, response.getBody().get(0).keyName());
    }

    @Test
    void testUpdateSettings() throws SettingException, NotCreatedException {
        // Arrange
        Map<String, String> formFields = Map.of(KEYNAME, KEYVALUE);
        Map<String, MultipartFile> files = Map.of();

        when(settingService.update(formFields, files)).thenReturn(List.of(settingDto));

        // Act
        ResponseEntity<List<SettingDto>> response = settingController.updateSettings(formFields, files);

        // Assert
        assertEquals(STATUSOK, response.getStatusCodeValue());
        assertEquals(ID, response.getBody().size());
        assertEquals(KEYVALUE, response.getBody().get(0).keyValue());
    }

    @Test
    void testFindAllByKeyNameThrowsException() throws SettingNotFoundException {
        // Arrange
        when(settingService.findAllByKeyName(List.of(KEYNAME))).thenThrow(new SettingNotFoundException("Not found"));

        // Act & Assert
        assertThrows(SettingNotFoundException.class, () -> settingController.findAllByKeyName(List.of(KEYNAME)));
    }


    @Test
    void testUpdateSettingsThrowsException() throws SettingException, NotCreatedException {
        // Arrange
        Map<String, String> formFields = Map.of(KEYNAME, KEYVALUE);
        Map<String, MultipartFile> files = Map.of();

        when(settingService.update(formFields, files)).thenThrow(new SettingException("Error updating"));

        // Act & Assert
        assertThrows(SettingException.class, () -> settingController.updateSettings(formFields, files));
    }
}
