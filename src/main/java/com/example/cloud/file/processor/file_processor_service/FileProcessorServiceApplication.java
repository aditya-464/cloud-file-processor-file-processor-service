package com.example.cloud.file.processor.file_processor_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FileProcessorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileProcessorServiceApplication.class, args);
	}

}
