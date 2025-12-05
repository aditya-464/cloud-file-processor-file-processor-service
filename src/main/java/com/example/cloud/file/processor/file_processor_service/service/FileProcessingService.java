package com.example.cloud.file.processor.file_processor_service.service;

import com.example.cloud.file.processor.file_processor_service.model.FileProcessed;
import com.example.cloud.file.processor.file_processor_service.repository.FileProcessingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

@Service
public class FileProcessingService {

    private final S3DownloadService s3DownloadService;
    private final FileProcessingRepository fileProcessingRepository;

    public FileProcessingService(S3DownloadService s3DownloadService, FileProcessingRepository fileProcessingRepository){
        this.s3DownloadService = s3DownloadService;
        this.fileProcessingRepository = fileProcessingRepository;
    }

    @Value("${aws.s3.bucket-name}")
    private String bucket;

    public void process(String s3Key, Long uploadFileId, String originalFileName) {
        FileProcessed pf = FileProcessed.builder()
                .uploadFileId(uploadFileId)
                .originalFileName(originalFileName)
                .s3Key(s3Key)
                .status("PROCESSING")
                .processedAt(LocalDateTime.now())
                .build();

        pf = fileProcessingRepository.save(pf);

        try {
            byte[] fileBytes = s3DownloadService.downloadAsBytes(bucket, s3Key);

            Metadata metadata = new Metadata();
            String detected = tika.detect(fileBytes);
            metadata.set(Metadata.CONTENT_TYPE, detected);

            // Use Tika to extract text for PDF, Word, Excel, plain text, etc.
            String extractedText;
            try (ByteArrayInputStream in = new ByteArrayInputStream(fileBytes)) {
                extractedText = tika.parseToString(in, metadata);
            }

            // Basic normalization: limit huge outputs (optional)
            String finalText = normalizeExtractedText(extractedText);

            pf.setProcessedContent(finalText);
            pf.setStatus("DONE");
            pf.setProcessedAt(LocalDateTime.now());
            pf.setContentType(detected);
            fileProcessingRepository.save(pf);

        } catch (Exception e) {
            pf.setStatus("FAILED");
            fileProcessingRepository.save(pf);
            // log properly in real app
            e.printStackTrace();
        }
    }


    private String normalizeExtractedText(String text) {
        if (text == null) return "";
        // Trim and optionally limit size - avoid storing enormous blobs without thinking:
        int MAX = 200_0000; // ~2MB of characters (adjust per your DB capabilities)
        if (text.length() > MAX) {
            return text.substring(0, MAX) + "\n\n[TRUNCATED]";
        }
        return text.trim();
    }

}
