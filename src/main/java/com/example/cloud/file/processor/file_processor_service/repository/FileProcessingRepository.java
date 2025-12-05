package com.example.cloud.file.processor.file_processor_service.repository;

import com.example.cloud.file.processor.file_processor_service.model.FileProcessed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileProcessingRepository extends JpaRepository<FileProcessed, Long> {
}
