package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.gallery.GalleryResponse;
import com.example.ShopAppEcomere.service.GalleryService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/galleries")
public class GalleryController {
    private final GalleryService galleryService;

    @GetMapping("/{id}")
    @ApiMessage("Fetch all gallery by product id")
    public ApiResponse<List<GalleryResponse>> getGalleryByProductId(@PathVariable Integer id) {
        return ApiResponse.<List<GalleryResponse>>builder()
                .result(galleryService.getGalleryByProductId(id))
                .build();
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiMessage("Create new gallery")
    public ApiResponse<GalleryResponse> createGallery(@PathVariable Integer id, @RequestParam MultipartFile file, @RequestParam Integer level) {
        return ApiResponse.<GalleryResponse>builder()
                .result(galleryService.createGallery(id, file, level))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiMessage("Delete gallery by id")
    public ApiResponse<Void> deleteGallery(@PathVariable Integer id) {
        galleryService.deleteGallery(id);
        return ApiResponse.<Void>builder().build();


    }
}
