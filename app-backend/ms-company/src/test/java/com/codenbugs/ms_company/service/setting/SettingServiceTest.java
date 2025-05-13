package com.codenbugs.ms_company.service.setting;

import com.codenbugs.ms_company.dtos.setting.SettingDto;
import com.codenbugs.ms_company.dtos.setting.SettingDvo;
import com.codenbugs.ms_company.dtos.setting.SettingModule;
import com.codenbugs.ms_company.exceptions.feign.NotCreatedException;
import com.codenbugs.ms_company.exceptions.setting.SettingException;
import com.codenbugs.ms_company.exceptions.setting.SettingNotFoundException;
import com.codenbugs.ms_company.exceptions.setting.SettingNotUpdatedException;
import com.codenbugs.ms_company.model.setting.Setting;
import com.codenbugs.ms_company.repositories.setting.SettingRepository;
import com.codenbugs.ms_company.services.setting.SettingServiceImpl;
import com.codenbugs.ms_company.utils.UploadFileComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SettingServiceTest {

    @Mock
    SettingRepository settingRepository;

    @Mock
    UploadFileComponent uploadFileComponent;

    @InjectMocks
    private SettingServiceImpl settingService;


    private final String KEY = "key";
    private final String VALUE = "value";
    private final String CONFIG_KEY = "config";
    private final String MISSING_KEY = "missing";
    private final String FILE_KEY = "fileKey";
    private final Integer ID = 1;
    private final String KEYNAME = "keyName";
    private final String KEYVALUE = "keyValue";
    private final String VALUETYPE = "valueType";
    private final String LABELVALUE = "labelValue";
    private final Boolean ENABLED = true;
    private final Integer FKSETTINGTYPE = 1;
    private final String SETTINGTYPENAME = "settingTypeName";


    private Setting enabledSetting;
    private Setting disabledSetting;
    private SettingDvo dvo1;
    private SettingDvo dvo2;
    private SettingDvo dvo3;

    @BeforeEach
    void setUp() {
        enabledSetting = new Setting();
        enabledSetting.setKeyName(KEY);
        enabledSetting.setIsEnabled(true);

        disabledSetting = new Setting();
        disabledSetting.setKeyName(KEY);
        disabledSetting.setIsEnabled(false);

        dvo1 = new SettingDvo(ID,KEYNAME,KEYVALUE,VALUETYPE,LABELVALUE,ENABLED,FKSETTINGTYPE,SETTINGTYPENAME);
        dvo2 = new SettingDvo(ID+1,KEYNAME,KEYVALUE,VALUETYPE,LABELVALUE,ENABLED,FKSETTINGTYPE,SETTINGTYPENAME);
        dvo3 = new SettingDvo(ID+2,KEYNAME,KEYVALUE,VALUETYPE,LABELVALUE,ENABLED,FKSETTINGTYPE,SETTINGTYPENAME);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<SettingDvo> dvos = List.of(dvo1, dvo2, dvo3);
        when(settingRepository.findAllByIsEnabled()).thenReturn(dvos);

        // Act
        List<SettingModule> result = settingService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(SETTINGTYPENAME, result.get(0).settingName());
        assertEquals(3, result.get(0).settings().size());
    }

    @Test
    void testFindByKeyNameFound() throws SettingNotFoundException {
        // Arrange
        when(settingRepository.findByKeyName(CONFIG_KEY)).thenReturn(Optional.of(enabledSetting));

        // Act
        Setting result = settingService.findByKeyName(CONFIG_KEY);

        // Assert
        assertEquals(KEY, result.getKeyName());
    }

    @Test
    void testFindByKeyNameNotFound() throws SettingNotFoundException {
        // Arrange
        when(settingRepository.findByKeyName(MISSING_KEY)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SettingNotFoundException.class, () -> settingService.findByKeyName(MISSING_KEY));
    }

    @Test
    void testUpdateWithValidSettings() throws SettingException, NotCreatedException {
        // Arrange
        Map<String, String> settings = Map.of(KEY, VALUE);
        Map<String, MultipartFile> files = Map.of();
        when(settingRepository.findByKeyName(KEY)).thenReturn(Optional.of(enabledSetting));
        when(settingRepository.save(any())).thenReturn(enabledSetting);

        // Act
        List<SettingDto> result = settingService.update(settings, files);

        // Assert
        assertEquals(1, result.size());
        assertEquals(KEY, result.get(0).keyName());
    }

    @Test
    void testUpdateThrowsWhenSettingDisabled() throws SettingNotFoundException {
        // Arrange
        Map<String, String> settings = Map.of(KEY, VALUE);
        when(settingRepository.findByKeyName(KEY)).thenReturn(Optional.of(disabledSetting));

        // Act & Assert
        assertThrows(SettingNotUpdatedException.class, () -> settingService.update(settings, Map.of()));
    }

    @Test
    void testFindAllByKeyNameSuccess() throws SettingNotFoundException {
        // Arrange
        Setting setting1 = new Setting();
        setting1.setKeyName(KEYNAME);
        Setting setting2 = new Setting();
        setting2.setKeyName(KEYNAME+1);
        when(settingRepository.findByKeyName(KEYNAME)).thenReturn(Optional.of(setting1));
        when(settingRepository.findByKeyName(KEYNAME+1)).thenReturn(Optional.of(setting2));

        // Act
        List<SettingDto> result = settingService.findAllByKeyName(List.of(KEYNAME, KEYNAME+1));

        // Assert
        assertEquals(2, result.size());
        assertEquals(KEYNAME, result.get(0).keyName());
    }

    @Test
    void testUpdateWithFile() throws SettingException, NotCreatedException {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(settingRepository.findByKeyName(FILE_KEY)).thenReturn(Optional.of(enabledSetting));
        when(uploadFileComponent.uploadFile(file)).thenReturn("uploaded.png");
        when(settingRepository.save(any())).thenReturn(enabledSetting);

        // Act
        List<SettingDto> result = settingService.update(Map.of(), Map.of(FILE_KEY, file));

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateWithFileThrowsWhenSettingDisabled() throws SettingNotFoundException {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(settingRepository.findByKeyName(FILE_KEY)).thenReturn(Optional.of(disabledSetting));

        // Act & Assert
        assertThrows(SettingNotUpdatedException.class, () -> settingService.update(Map.of(), Map.of(FILE_KEY, file)));
    }
}
