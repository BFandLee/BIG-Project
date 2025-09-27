package com.hk.backendweb02.post.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("업로드할 파일을 선택해주세요.");
        }

        // 고유한 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        try {
            // 폴더가 없으면 생성
            Files.createDirectories(filePath.getParent());
            // 파일 저장
            Files.write(filePath, file.getBytes());
            // 외부에서 접근 가능한 URL 경로 반환
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일을 저장할 수 없습니다. " + fileName, e);
        }
    }
}