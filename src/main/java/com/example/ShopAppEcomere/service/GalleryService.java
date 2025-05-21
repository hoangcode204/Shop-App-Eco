package com.example.ShopAppEcomere.service;
import com.example.ShopAppEcomere.dto.response.gallery.GalleryResponse;
import com.example.ShopAppEcomere.entity.Gallery;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.GalleryMapper;
import com.example.ShopAppEcomere.repository.GalleryRepository;
import com.example.ShopAppEcomere.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final GalleryMapper galleryMapper;
    private final ProductRepository productRepository;
    private final CloudinaryImageService cloudinaryImageService;
    public List<GalleryResponse> getGalleryByProductId(Integer id){
        List<Gallery> galleries = galleryRepository.getGalleriesByProductId(id);

        // Kiểm tra xem product có tồn tại không
        boolean productExists = productRepository.existsById(id);
        if (!productExists) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }

        // Nếu product có tồn tại nhưng không có ảnh => trả về danh sách trống
        return galleries.stream()
                .map(galleryMapper::toGalleryResponse)
                .collect(Collectors.toList());
    }
    public GalleryResponse createGallery(Integer productId, MultipartFile file, Integer level) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Upload ảnh lên Cloudinary và lấy URL
        String imageUrl = cloudinaryImageService.upload(file);

        // Tạo mới Gallery
        Gallery gallery = new Gallery();
        gallery.setProduct(product);
        gallery.setImg(imageUrl);
        gallery.setLevel(level);

        // Lưu vào database
        return galleryMapper.toGalleryResponse(galleryRepository.save(gallery));
    }
    public void deleteGallery(Integer id) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GALLERY_NOT_EXISTED));

        // Xóa ảnh trên Cloudinary
        cloudinaryImageService.delete(gallery.getImg());

        // Xóa Gallery trong database
        galleryRepository.delete(gallery);
    }
}
