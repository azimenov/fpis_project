package org.example.fpis_project.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.endpoint:#{null}}")
    private String endpoint;

    public String uploadFile(MultipartFile file, String folderName) {
        try {
            String fileName = generateFileName(file);
            String key = folderName + "/" + fileName;

            log.debug("Uploading file {} to S3 bucket {} in folder {}", fileName, bucketName, folderName);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(
                    bucketName,
                    key,
                    file.getInputStream(),
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));

            String fileUrl;
            if (endpoint != null && !endpoint.isEmpty()) {
                fileUrl = endpoint + "/" + bucketName + "/" + key;
            } else {
                fileUrl = amazonS3.getUrl(bucketName, key).toString();
            }

            log.info("File uploaded successfully to {}", fileUrl);
            return fileUrl;
        } catch (IOException e) {
            log.error("Failed to upload file to S3", e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            log.debug("Deleting file with key {} from S3 bucket {}", key, bucketName);

            amazonS3.deleteObject(bucketName, key);
            log.info("File deleted successfully from S3: {}", fileUrl);
        } catch (Exception e) {
            log.error("Failed to delete file from S3: {}", fileUrl, e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    }

    private String extractKeyFromUrl(String fileUrl) {
        if (endpoint != null && !endpoint.isEmpty() && fileUrl.startsWith(endpoint)) {
            // LocalStack URL format: http://localhost:4566/bucket-name/key
            String path = fileUrl.substring(endpoint.length() + 1);
            return path.substring(path.indexOf('/') + 1);
        } else {
            // AWS URL format: https://bucket-name.s3.region.amazonaws.com/key
            return fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);
        }
    }
}