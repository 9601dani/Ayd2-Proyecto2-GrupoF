package com.codenbugs.ms_company.utils;

import com.codenbugs.ms_company.client.UploadRestClient;
import com.codenbugs.ms_company.exceptions.feign.NotCreatedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
@AllArgsConstructor
public class UploadFileComponent {

    private final UploadRestClient uploadRestClient;

    public String uploadFile(@RequestPart("file") MultipartFile file) throws NotCreatedException {
        Map<String, String> fileMap = this.uploadRestClient.uploadImage(file);
        return fileMap.get("objectName");
    }
}
