package com.obigo.microev.tms.core.domain.service;

import com.obigo.microev.tms.core.domain.enumeration.AttachSubDirectory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {
    Long uploadFirstAttachFile(List<MultipartFile> multipartFiles, AttachSubDirectory attachSubDirectory, Long userSeq) throws Exception;
}
