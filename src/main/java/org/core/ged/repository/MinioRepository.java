package org.core.ged.repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.core.ged.config.MinioConfig;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MinioRepository {
    
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @SneakyThrows(Exception.class)
    public String createFolder(String parentFolder, String folderName){
        String fullPath = (parentFolder != null && !parentFolder.trim().isEmpty())
            ? parentFolder + folderName + "/"
            : folderName + "/";

        log.info("Creating folder at path: {}", fullPath);

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(minioConfig.getMinioBucketName())
                .object(fullPath)
                .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .build()
        );
        return fullPath;
    }

    @SneakyThrows(Exception.class)
    public String createFile(String folderPath, String fileName, InputStream content, String mimeType){
        String fullPath = (folderPath != null && !folderPath.trim().isEmpty())
            ? folderPath + fileName
            : fileName;

        log.info("Creating file at path: {}", fullPath);

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(minioConfig.getMinioBucketName())
                .object(fullPath)
                .stream(content, content.available(), -1)
                .contentType(mimeType)
                .build()
        );
        return fullPath;
    }

    @SneakyThrows(Exception.class)
    public Resource getFile(String filePath){
        log.info("Retrieving file from path: {}", filePath);
        InputStream inputStream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(minioConfig.getMinioBucketName())
                .object(filePath)
                .build()
        );
        return new InputStreamResource(inputStream);
    }

    @SneakyThrows(Exception.class)
    public void deleteFile(String filePath){
        log.info("Deleting file at path: {}", filePath);
        Iterable<Result<Item>> results = minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(minioConfig.getMinioBucketName())
                .prefix(filePath)
                .recursive(true)
                .build()
        );
        for(Result<Item> result : results){
            Item item = result.get();
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(minioConfig.getMinioBucketName())
                    .object(item.objectName())
                    .build()
            );
        }
    }

    @SneakyThrows(Exception.class)
    public boolean fileExists(String filePath){
        log.info("Checking if file exists at path: {}", filePath);

        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(minioConfig.getMinioBucketName())
                    .object(filePath)
                    .build()
                );
        } catch (ErrorResponseException e) {
            if(e.response().code() == 404){
                log.warn("File not found at path: {}", filePath);
                return false; // File does not exist
            }
            log.error("Error checking file existence at path: {}", filePath, e);
            throw e; // Re-throw other exceptions
        }
        log.info("File exists at path: {}", filePath);
        return true;
    }

}
