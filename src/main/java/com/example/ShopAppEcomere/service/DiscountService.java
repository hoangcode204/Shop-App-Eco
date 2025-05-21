package com.example.ShopAppEcomere.service;


import com.example.ShopAppEcomere.dto.response.discount.DiscountResponse;
import com.example.ShopAppEcomere.entity.Discount;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.DiscountMapper;
import com.example.ShopAppEcomere.repository.DiscountRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DiscountService {
private final DiscountRepository discountRepository;
private final DiscountMapper discountMapper;
    public List<DiscountResponse> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        return discounts.stream()
                .map(discountMapper::toDiscountResponse)
                .collect(Collectors.toList());
    }
    public DiscountResponse getDiscountByName(String name){
        Discount discount=discountRepository.findDiscountByName(name);
        return discountMapper.toDiscountResponse(discount);
    }
    public DiscountResponse getDiscountById(Integer id){
        Discount discount=discountRepository.getDiscountById(id);
        if(discount==null){
            return null;
        }
        return discountMapper.toDiscountResponse(discount);
    }
    public DiscountResponse create(Discount discountRequest){
        LocalDateTime date = LocalDateTime.now();
        Discount newDiscount=new Discount();
        newDiscount.setIs_active(true);
        newDiscount.setCreatedAt(date);
        newDiscount.setName(discountRequest.getName());
        newDiscount.setDescription(discountRequest.getDescription());
        newDiscount.setDiscount_percent(discountRequest.getDiscount_percent());
        return discountMapper.toDiscountResponse(discountRepository.save(newDiscount));
    }
    public DiscountResponse update(Discount discountUpdate,Integer id){
        Discount discount = discountRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        discount.setName(discountUpdate.getName());
        discount.setDescription(discountUpdate.getDescription());
        discount.setDiscount_percent(discountUpdate.getDiscount_percent());
        discount.setIs_active(discountUpdate.getIs_active());
        discount.setUpdatedAt(LocalDateTime.now());
        return discountMapper.toDiscountResponse(discountRepository.save(discount));
    }
}
