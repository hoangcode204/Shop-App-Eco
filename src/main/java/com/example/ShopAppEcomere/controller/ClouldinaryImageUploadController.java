package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.service.CloudinaryImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@RestController
@RequestMapping("/api/v1/cloudinary/upload")
@AllArgsConstructor
public class ClouldinaryImageUploadController {
    private final CloudinaryImageService cloudinaryImageService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')or hasAuthority('ROLE_USER')")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        String data = cloudinaryImageService.upload(file);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
