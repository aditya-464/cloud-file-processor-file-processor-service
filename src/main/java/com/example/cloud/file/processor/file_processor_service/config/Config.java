package com.example.cloud.file.processor.file_processor_service.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class Config {

    @Bean
    public S3Client s3Client(){
        return S3Client.builder().build();
    }

    @Bean
    public Tika tika() {
        return new Tika();
    }
}
