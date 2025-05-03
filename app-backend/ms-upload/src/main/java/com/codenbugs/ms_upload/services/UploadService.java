package com.codenbugs.ms_upload.services;

import com.codenbugs.ms_upload.exceptions.NotCreatedException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Bucket;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UploadService {

    private final Storage storage;
    private final String bucketName;

    public String uploadFile(MultipartFile file, String path) throws NotCreatedException {
        try {

            String objectName = path + "/" + UUID.randomUUID();
            Bucket bucket = storage.get(bucketName);
            bucket.create(objectName, file.getBytes(), file.getContentType());
            return objectName;
        } catch(IOException exception) {
            throw new NotCreatedException("No se pudo guardar la imagen");
        }
    }
}
