package com.example.cloud.file.processor.file_processor_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileProcessed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long uploadFileId;
    private String originalFileName;
    private String s3Key;
    private String contentType;

    @Column(columnDefinition = "TEXT")
    private String processedContent; // extracted text / summary

    @Column(columnDefinition = "TEXT")
    private String tableData; // JSON string for Excel sheets

    private String status; // PROCESSING / SUCCESS / FAILED
    private LocalDateTime processedAt;
}
