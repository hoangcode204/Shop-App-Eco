package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.CategoryRequest;
import com.example.ShopAppEcomere.dto.response.category.CategoryDetailResponse;
import com.example.ShopAppEcomere.dto.response.category.CategoryFetchResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.entity.Category;
import com.example.ShopAppEcomere.entity.Product;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.CategoryMapper;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;
    private List<Product> fetchProductsFromIds(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED); // Ném exception
        }
        List<Product> productList=productRepository.findAllById(productIds);
        if (productList.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED); // Ném exception
        }

        return productList;
    }
    public CategoryDetailResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        // Lấy danh sách sản phẩm hợp lệ từ productId
        List<Product> products = fetchProductsFromIds(request.getProductId());

        // Chuyển từ DTO sang Entity
        Category category = categoryMapper.toCategory(request);
        category.setProducts(products);

        // Lưu vào cơ sở dữ liệu
        Category savedCategory = categoryRepository.save(category);

        // Trả về Response
        return mapToCategoryDetailResponse(savedCategory);
    }
    public CategoryDetailResponse updateCategory(CategoryRequest categoryRequest, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        // Cập nhật thông tin cơ bản
        categoryMapper.updateCategory(category, categoryRequest);

        // Lấy danh sách sản phẩm hợp lệ từ productId
        List<Product> products = fetchProductsFromIds(categoryRequest.getProductId());

        // Duy trì tham chiếu danh sách cũ và cập nhật
        List<Product> currentProducts = category.getProducts();

        // Xóa các sản phẩm không còn trong danh sách mới
        currentProducts.removeIf(product -> !products.contains(product));

        // Thêm các sản phẩm mới chưa có trong danh sách
        for (Product product : products) {
            if (!currentProducts.contains(product)) {
                product.setCategory(category); // Cập nhật liên kết ngược
                currentProducts.add(product);
            }
        }

        // Lưu thay đổi
        Category updatedCategory = categoryRepository.save(category);

        return mapToCategoryDetailResponse(updatedCategory);
    }

    public CategoryDetailResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        // Kiểm tra nếu active là null thì mặc định là false
        Boolean isActive = category.getActive() != null ? category.getActive() : false;

        if (!isActive) {
            return mapToCategoryDetailResponse(category);
        } else {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }
    }

    private CategoryDetailResponse mapToCategoryDetailResponse(Category category) {
        return CategoryDetailResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .products(category.getProducts().stream()
                        .filter(product -> product != null) // Loại bỏ các phần tử null
                        .filter(product -> Boolean.TRUE.equals(product.getActive())) // Chỉ lấy sản phẩm có active = true
                        .map(product -> CategoryDetailResponse.ProductBasicResponse.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .active(product.getActive())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
          category.setActive(false);
        categoryRepository.save(category);
    }

    public ResultPaginationDTO fetchAllCategory(Specification<Category> spec, Pageable pageable) {
        Page<Category> pageCategory = categoryRepository.findAll(spec, pageable);
        // Chuyển đổi danh sách Category sang BasicCategoryResponse
        List<CategoryFetchResponse> basicCategories = pageCategory.getContent()
                .stream()
                .map(category -> new CategoryFetchResponse(category.getId(), category.getName()))
                .toList();

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCategory.getTotalPages());
        mt.setTotal(pageCategory.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(basicCategories); // Set danh sách BasicCategoryResponse vào result

        return rs;
    }
}
