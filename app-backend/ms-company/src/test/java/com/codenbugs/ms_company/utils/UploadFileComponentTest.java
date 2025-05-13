package com.codenbugs.ms_company.utils;

import com.codenbugs.ms_company.client.UploadRestClient;
import com.codenbugs.ms_company.exceptions.feign.NotCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UploadFileComponentTest {

    private final String FILE_NAME = "test.png";
    private final String OBJECT_KEY = "objectName";
    private final String OBJECT_VALUE = "bucket/test.png";

    @Mock
    private UploadRestClient uploadRestClient;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UploadFileComponent uploadFileComponent;

    @BeforeEach
    void setUp() {
        lenient().when(multipartFile.getOriginalFilename()).thenReturn(FILE_NAME);
    }

    @Test
    void testUploadFileSuccess() throws NotCreatedException {
        // Arrange
        Map<String, String> responseMap = Map.of(OBJECT_KEY, OBJECT_VALUE);
        when(uploadRestClient.uploadImage(multipartFile)).thenReturn(responseMap);

        // Act
        String result = uploadFileComponent.uploadFile(multipartFile);

        // Assert
        assertEquals(OBJECT_VALUE, result);
    }

    @Test
    void testUploadFileThrowsException() throws NotCreatedException {
        // Arrange
        when(uploadRestClient.uploadImage(multipartFile)).thenThrow(new NotCreatedException("Upload failed"));

        // Act & Assert
        assertThrows(NotCreatedException.class, () -> uploadFileComponent.uploadFile(multipartFile));
    }
}
