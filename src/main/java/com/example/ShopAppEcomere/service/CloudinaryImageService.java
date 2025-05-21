package com.example.ShopAppEcomere.service;

import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor

public class CloudinaryImageService {
    private final Cloudinary cloudinary;

    // **1. Upload ảnh lên Cloudinary và trả về URL**
    public String upload(MultipartFile file) {
        try {
            Map params = ObjectUtils.asMap(
                    "folder", "myfolder/avatar"
            );
            Map<String, Object> data = cloudinary.uploader().upload(file.getBytes(), params);

            // Trả về URL của ảnh đã upload
            return (String) data.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed!");
        }
    }

    // **2. Xóa ảnh theo public_id**
    public boolean delete(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image!");
        }
    }

    // **3. Cập nhật ảnh (Xóa ảnh cũ rồi upload ảnh mới)**
    public String update(String oldPublicId, MultipartFile newFile) {
        // Xóa ảnh cũ trước khi upload ảnh mới
        if (oldPublicId != null) {
            delete(oldPublicId);
        }
        return upload(newFile);
    }
}
