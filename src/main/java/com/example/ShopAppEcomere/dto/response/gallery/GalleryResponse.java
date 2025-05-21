package com.example.ShopAppEcomere.dto.response.gallery;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GalleryResponse {
    private Integer id;

    private Integer level;

    private String img;
}
