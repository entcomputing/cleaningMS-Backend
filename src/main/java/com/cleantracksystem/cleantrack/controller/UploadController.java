package com.cleantracksystem.cleantrack.controller;

import com.cleantracksystem.cleantrack.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private final FileStorageService fileStorageService;

    public UploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // Public: part of the unauthenticated cleaning-submission flow (clean.tsx
    // uploads a photo before the record/issue exist yet). Returns the path to
    // hand back as an issue's photo_url.
    @PostMapping("/issue-photos")
    public ResponseEntity<Map<String, String>> uploadIssuePhoto(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String path = fileStorageService.storeIssuePhoto(file);
        return ResponseEntity.ok(Map.of("path", path));
    }
}
