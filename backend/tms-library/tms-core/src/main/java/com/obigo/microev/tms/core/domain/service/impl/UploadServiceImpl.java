package com.obigo.microev.tms.core.domain.service.impl;

import com.obigo.microev.tms.core.domain.entity.Attach;
import com.obigo.microev.tms.core.domain.entity.File;
import com.obigo.microev.tms.core.domain.enumeration.AttachSubDirectory;
import com.obigo.microev.tms.core.domain.mapper.AttachMapper;
import com.obigo.microev.tms.core.domain.mapper.FileMapper;
import com.obigo.microev.tms.core.domain.service.UploadService;
import com.obigo.microev.tms.core.domain.vo.S3File;
import com.obigo.microev.tms.core.util.S3FileUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final S3FileUploader s3FileUploader;
    private final AttachMapper attachMapper;
    private final FileMapper fileMapper;


    /**
     * 첨부파일 업로드
     * @param multipartFiles
     * @param attachSubDirectory
     * @param userSeq
     * @return
     * @throws Exception
     */
    @Override
    public Long uploadFirstAttachFile(List<MultipartFile> multipartFiles, AttachSubDirectory attachSubDirectory, Long userSeq) throws Exception {

        LocalDateTime now = LocalDateTime.now();

        // 파일 업로드
        List<S3File> s3Files = s3FileUploader.uploadFilesToS3Bucket(multipartFiles, attachSubDirectory.name());
        if (s3Files.isEmpty()) {
            throw new Exception("Failed to upload files to S3 bucket");
        }

        // 파일 정보 DB에 저장
        Attach attach = Attach.builder()
                .createdAt(now)
                .creatorSeq(userSeq)
                .build();
        attachMapper.insert(attach);

        int sortOrder = 1;
        for (S3File s3File : s3Files) {
            File file = File.builder()
                    .attachSeq(attach.getAttachSeq())
                    .fileFullPath(s3File.getFileFullPath())
                    .originalFileName(s3File.getOriginalFileName())
                    .sortOrder(sortOrder++)
                    .build();
            fileMapper.insert(file);
        }

        return attach.getAttachSeq();
    }
}
