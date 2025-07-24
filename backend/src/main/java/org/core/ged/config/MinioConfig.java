package org.core.ged.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import lombok.Getter;

@Configuration
@Getter
public class MinioConfig {
    
    @Value("${minio.url}")
    private String minioUrl;
    
    @Value("${minio.access-key}")
    private String minioAccessKey;
    
    @Value("${minio.secret-key}")
    private String minioSecretKey;
    
    @Value("${minio.bucket-name}")
    private String minioBucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(minioAccessKey, minioSecretKey)
            .build();
    }
}
