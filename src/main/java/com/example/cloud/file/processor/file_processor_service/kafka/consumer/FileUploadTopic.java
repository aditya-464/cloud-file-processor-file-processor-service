package com.example.cloud.file.processor.file_processor_service.kafka.consumer;

import com.example.cloud.file.processor.file_processor_service.service.FileProcessingService;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FileUploadTopic {

    private final FileProcessingService fileProcessingService;

    public FileUploadTopic(FileProcessingService fileProcessingService){
        this.fileProcessingService = fileProcessingService;
    }

    @KafkaListener(topics = "${kafka.topic.file-upload}", groupId = "${kafka.group-id}")
    public void consume(String message) throws Exception{
        JSONObject json = new JSONObject(message);
        Long uploadFileId = json.has("fileId") ? json.getLong("fileId") : null;
        String s3Key = json.getString("s3Key"); // assume upload service sends key, not full url
        String fileName = json.optString("fileName", "unknown");

        fileProcessingService.process(s3Key, uploadFileId, fileName);
    }
}
