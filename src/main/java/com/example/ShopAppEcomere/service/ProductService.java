package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.ProductRequest;
import com.example.ShopAppEcomere.dto.response.product.ProductBasicResponse;
import com.example.ShopAppEcomere.dto.response.product.ProductDetailResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.entity.Category;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.ProductMapper;
import com.example.ShopAppEcomere.repository.CategoryRepository;
import com.example.ShopAppEcomere.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    public ProductDetailResponse createProduct(ProductRequest productRequest){
        // Tìm danh mục nếu có
        Category category = null;
        if (productRequest.getCategoryId() != null) {
            category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        }
        if(productRepository.existsByName(productRequest.getName())){
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        // Sử dụng mapper để chuyển từ ProductRequest sang Product
        Product product = productMapper.toProduct(productRequest);
        product.setCategory(category);

        // Lưu sản phẩm
        Product savedProduct = productRepository.save(product);

        // Sử dụng mapper để chuyển sang ProductResponse
        return mapToProductDetailResponse(savedProduct);
    }
    public ProductDetailResponse getProductById(Long id) {
        return  mapToProductDetailResponse(productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)));
    }
    public ProductDetailResponse mapToProductDetailResponse(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .stock(product.getStock())
                .active(product.getActive())
                .category(
                        product.getCategory() != null ?
                                ProductDetailResponse.CategoryResponse.builder()
                                        .id(product.getCategory().getId())
                                        .name(product.getCategory().getName())
                                        .build()
                                : null
                )
                .build();
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        product.setActive(false);
            productRepository.save(product);

    }

    public ProductDetailResponse updateProduct(ProductRequest request, Long productId) {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        }
        Product product= productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        productMapper.updateProduct(product,request);
        product.setCategory(category);
        return  mapToProductDetailResponse(productRepository.save(product));
    }

    public ResultPaginationDTO fetchAllProduct(Specification<Product> spec, Pageable pageable) {
        Page<Product> pageProduct = this.productRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageProduct.getTotalPages());
        mt.setTotal(pageProduct.getTotalElements());

        rs.setMeta(mt);

        // Chuyển đổi từ Product sang ProductBasicResponse
        List<ProductBasicResponse> listProduct = pageProduct.getContent()
                .stream()
                .map(this::mapToProductBasicResponse) // Sử dụng hàm mapToProductBasicResponse
                .collect(Collectors.toList());
        rs.setResult(listProduct);
        return rs;

    }
    private ProductBasicResponse mapToProductBasicResponse(Product product) {
        return ProductBasicResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .stock(product.getStock())
                .active(product.getActive())
                .build();
    }
}
