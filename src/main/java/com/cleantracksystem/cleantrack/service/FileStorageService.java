package com.cleantracksystem.cleantrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.root = Path.of(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create upload directory: " + root, e);
        }
    }

    /** Saves the file under a generated name and returns that name (what gets stored as photo_url). */
    public String storeIssuePhoto(MultipartFile file) {
        String extension = extensionOf(file.getOriginalFilename());
        String filename = UUID.randomUUID() + extension;
        Path target = root.resolve(filename).normalize();
        if (!target.startsWith(root)) {
            throw new IllegalArgumentException("Invalid file name");
        }
        try {
            file.transferTo(target);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not store uploaded file", e);
        }
        return filename;
    }

    private String extensionOf(String originalFilename) {
        if (originalFilename == null) return "";
        int dot = originalFilename.lastIndexOf('.');
        if (dot < 0 || dot == originalFilename.length() - 1) return "";
        String ext = originalFilename.substring(dot).toLowerCase();
        // Keep this to a plain extension - no path separators, no oversized junk.
        if (ext.length() > 10 || ext.contains("/") || ext.contains("\\")) return "";
        return ext;
    }
}
