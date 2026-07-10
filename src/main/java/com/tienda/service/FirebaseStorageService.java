package com.tienda.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseStorageService {

    private final ObjectProvider<Storage> storageProvider;

    @Value("${firebase.bucket.name:}")
    private String bucketName;

    @Value("${firebase.storage.path:tienda}")
    private String storagePath;

    public FirebaseStorageService(ObjectProvider<Storage> storageProvider) {
        this.storageProvider = storageProvider;
    }

    public String uploadImage(MultipartFile localFile, String folder, Integer id) throws IOException {
        Storage storage = storageProvider.getIfAvailable();
        if (storage == null || bucketName == null || bucketName.isBlank()) {
            throw new IOException("Firebase no está configurado. Active FIREBASE_ENABLED y configure sus credenciales.");
        }

        String originalName = localFile.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        String fileName = "img" + String.format("%014d", id) + extension;
        String objectName = storagePath + "/" + folder + "/" + fileName;
        BlobId blobId = BlobId.of(bucketName, objectName);
        String contentType = localFile.getContentType() == null ? "application/octet-stream" : localFile.getContentType();
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        storage.create(blobInfo, localFile.getBytes());
        return storage.signUrl(blobInfo, 1825, TimeUnit.DAYS).toString();
    }
}
