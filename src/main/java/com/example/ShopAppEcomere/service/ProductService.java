package com.example.ShopAppEcomere.service;


import com.example.ShopAppEcomere.dto.request.ProductRequest;
import com.example.ShopAppEcomere.dto.response.product.ProductResponse;
import com.example.ShopAppEcomere.entity.Category;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.ProductMapper;
import com.example.ShopAppEcomere.repository.CategoryRepository;
import com.example.ShopAppEcomere.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CloudinaryImageService cloudinaryImageService;
    public Page<ProductResponse> getAllProduct(HashMap<String, String> multipleParam){
        Integer page;
        Integer size;
        if(multipleParam.get("page") == null) {
            page = 0;
        } else {
            page = Integer.parseInt(multipleParam.get("page"));
        }

        if(multipleParam.get("size") == null) {
            size=8;
        } else {
            size = Integer.parseInt(multipleParam.get("size"));
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        // Chuyển đổi từ Page<Product> -> Page<ProductResponse>
        return productPage.map(productMapper::toProductResponse);
    }
    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.getAllProduct();
        return products.stream().map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    public ProductResponse getProductById(Integer id){
        Product product=productRepository.findProductById(id);
        if(product==null){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> findProductByPriceBetween(Integer priceMin, Integer priceMax) {
        List<Product> products= productRepository.findProductByPriceBetween(priceMin, priceMax);
        return products.stream().map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    public List<ProductResponse> getProductByName(String name) {
        List<Product> products=productRepository.findProductByName(name);
        return products.stream().map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    public ProductResponse create(ProductRequest productRequest, MultipartFile file) {

        // Tìm category từ ID
        Category category = categoryRepository.findById(productRequest.getCategory_id())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        // **1. Upload ảnh lên Cloudinary**
        String imageUrl = cloudinaryImageService.upload(file);

        // **2. Gán URL ảnh vào UserRequest**
          productRequest.setImg(imageUrl);

        // Chuyển từ request → entity
        Product newProduct = productMapper.toProduct(productRequest);
        newProduct.setCategory(category);

        // Chuyển từ entity → response
        return productMapper.toProductResponse(productRepository.save(newProduct));
    }
    public void delete(Product productRequest) {
        productRequest.setDeletedAt(new Date());
        productRepository.save(productRequest);
    }
    public ProductResponse update( Integer id,ProductRequest productRequest,MultipartFile file) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        product=productMapper.toProduct(productRequest);
        product.setCategory(categoryRepository.findById(productRequest.getCategory_id())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
        // Cập nhật ảnh nếu có file
        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryImageService.upload(file);
            product.setImg(imageUrl);
        }
        return productMapper.toProductResponse(productRepository.save(product));
    }
    public ProductResponse deleteProductById(Integer id) {
        Product product = productRepository.findProductById(id);
        if (product.getId() != null) {
            Date currenDate = new Date();
            product.setDeletedAt(currenDate);
            productRepository.save(product);
        } else {
            return null;
        }
        return productMapper.toProductResponse(product);
    }
    public List<ProductResponse> getProductsByCategory(Integer id) {
        List<Product> products=productRepository.getProductsByCategory(id);
        return products.stream().map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

public Page<ProductResponse> getFilteredProducts(int page, int limit, String sortBy, String name, String category, Float priceMax, Float priceMin, String order) {
    Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by(direction, sortBy));

    // Cách 1: Sử dụng `@Query` trong repository
    Page<Product> productPage = productRepository.findFilteredProducts(name, category, priceMin, priceMax, pageRequest);
    return productPage.map(productMapper::toProductResponse);
}


    private PageRequest createPageRequest(int page, int limit, String sortBy, String order) {
        Sort.Direction sortDirection = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort;
        switch (sortBy) {
            case "price":
                sort = Sort.by(sortDirection, "price");
                break;
            case "createdAt":
                sort = Sort.by(sortDirection, "created_at");
                break;
            case "view":
                sort = Sort.by(sortDirection, "id");
                break;
            case "sold":
                sort = Sort.by(sortDirection, "quantity");
                break;
            default:
                sort = Sort.by(sortDirection, "created_at");
        }

        return PageRequest.of(page - 1, limit, sort);
    }
}
