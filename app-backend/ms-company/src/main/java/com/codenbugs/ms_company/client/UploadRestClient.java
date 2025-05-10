package com.codenbugs.ms_company.client;

import com.codenbugs.ms_company.exceptions.feign.NotCreatedException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "ms-upload", url = "http://localhost:8010/v1/uploads")
public interface UploadRestClient {

    @PostMapping(value = "/images", consumes = "multipart/form-data")
    Map<String, String> uploadImage(@RequestPart("file") MultipartFile file) throws NotCreatedException;
}
