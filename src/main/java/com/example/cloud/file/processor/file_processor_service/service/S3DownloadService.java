package com.example.cloud.file.processor.file_processor_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.nio.charset.StandardCharsets;

@Service
public class S3DownloadService {
    private final S3Client s3Client;

    public S3DownloadService(S3Client s3Client){
        this.s3Client = s3Client;
    }

    public byte[] downloadAsBytes(String bucket, String key) {
        ResponseBytes<GetObjectResponse> bytes = s3Client.getObjectAsBytes(
                GetObjectRequest.builder().bucket(bucket).key(key).build()
        );
        return bytes.asByteArray();
    }

    public String downloadAsString(String bucket, String key) {
        byte[] bytes = downloadAsBytes(bucket, key);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
