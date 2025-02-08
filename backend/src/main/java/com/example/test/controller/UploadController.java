package com.example.test.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.response.ResponseUploadFile;
import com.example.test.service.UploadService;

import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/v1/files")
public class UploadController {
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseUploadFile> uploadImage(@RequestParam("fileUpload") MultipartFile file)
            throws IOException, BadRequestException {

        String contentType = file.getContentType();

        if (file.getSize() > 1048576) {
            throw new BadRequestException("File quá lớn, chỉ chấp nhận file dưới 1MB");
        }

        if (contentType == null ||
                !(contentType.equals("image/png") || contentType.equals("image/jpeg")
                        || contentType.equals("image/webp"))) {
            throw new BadRequestException("Chỉ chấp nhận file dạng png,jpg,webp");

        }

        Map uploadResult = uploadService.uploadFile(file);
        ResponseUploadFile res = new ResponseUploadFile(uploadResult.get("url").toString());
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(uploadService.uploadVideo(file));
    }
}
