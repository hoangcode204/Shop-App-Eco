package com.example.ShopAppEcomere.dto.response.category;
import com.example.ShopAppEcomere.entity.Product;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFetchResponse {

    private Long id;
     private String name;


}
