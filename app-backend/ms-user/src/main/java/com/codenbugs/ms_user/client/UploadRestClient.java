package com.codenbugs.ms_user.client;

import com.codenbugs.ms_user.exceptions.user.upload.NotCreatedException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@FeignClient(name = "ms-upload", url = "http://localhost:8010/v1/uploads")
public interface UploadRestClient {

    @PostMapping(value = "/images" , consumes = "multipart/form-data")
    HashMap<String, String> uploadImage(@RequestPart("file") MultipartFile file) throws NotCreatedException;
}
