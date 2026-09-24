package com.obigo.microev.tms.core.util;

import com.obigo.microev.tms.core.domain.vo.S3File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3FileUploader {

    @Value("${aws.s3.bucket}")
    private String s3BucketName;

    private final S3Client s3Client;

    /**
     * S3 버킷에 파일 업로드
     *
     * @param multipartFiles
     * @param subDirectory
     * @return
     * @throws Exception
     */
    public List<S3File> uploadFilesToS3Bucket(List<MultipartFile> multipartFiles, String subDirectory) throws Exception {
        log.info("Uploading file to S3 bucket: {}", s3BucketName);

        int sortOrder = 1;
        List<S3File> s3Files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            log.info("Uploading file: {}", multipartFile.getOriginalFilename());

            String contentType = multipartFile.getContentType();
            String originalFileName = multipartFile.getOriginalFilename();
            String fullPath = subDirectory + "/"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS"))
                    + "_" + originalFileName;


            try {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(s3BucketName)
                        .key(fullPath)
                        .contentType(contentType)
                        .contentLength(multipartFile.getSize())
                        .build();
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));

                S3File s3File = S3File.builder()
                        .fileFullPath(fullPath)
                        .originalFileName(originalFileName)
                        .sortOrder(sortOrder++)
                        .build();
                s3Files.add(s3File);

            } catch (Exception e) {
                log.error("Error uploading file to S3: {}, {}", (fullPath + "/" + originalFileName), e.getMessage());
                throw e;
            }
        }

        return s3Files;
    }

}
