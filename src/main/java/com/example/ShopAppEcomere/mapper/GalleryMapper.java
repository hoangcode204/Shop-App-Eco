package com.example.ShopAppEcomere.mapper;
import com.example.ShopAppEcomere.dto.response.gallery.GalleryResponse;
import com.example.ShopAppEcomere.entity.Gallery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GalleryMapper {
    // Chuyển đổi từ Gallery sang Gallery Response
   GalleryResponse toGalleryResponse(Gallery gallery);
}
