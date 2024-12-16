package com.example.ShopAppEcomere.service;

import com.example.ShopAppEcomere.dto.request.ShipmentRequest;
import com.example.ShopAppEcomere.dto.response.ShipmentResponse;
import com.example.ShopAppEcomere.dto.response.ResultPaginationDTO;
import com.example.ShopAppEcomere.entity.Shipment;
import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.enums.ShipmentStatusEnum;
import com.example.ShopAppEcomere.exception.AppException;
import com.example.ShopAppEcomere.exception.ErrorCode;
import com.example.ShopAppEcomere.mapper.ShipmentMapper;
import com.example.ShopAppEcomere.repository.ShipmentRepository;
import com.example.ShopAppEcomere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final UserRepository userRepository;

    public ShipmentResponse createShipment(ShipmentRequest request) {
        User user=null;
        if(request.getUserId()!=null){
            user=userRepository.findById(request.getUserId())
                    .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        }
        Shipment newShipment = shipmentMapper.toShipment(request);
        newShipment.setUser(user);
        return shipmentMapper.toShipmentResponse(shipmentRepository.save(newShipment));
    }
    @PostAuthorize("returnObject.userId == authentication.token.claims['userId']")
    public ShipmentResponse getShipmentById(Long id) {
        return shipmentMapper.toShipmentResponse(shipmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHIPMENT_NOT_EXISTED)));
    }
    @PreAuthorize("hasRole('USER')")
    public void deleteShipment(Long shipmentId) throws AccessDeniedException {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new AppException(ErrorCode.SHIPMENT_NOT_EXISTED));
        // Kiểm tra nếu email người dùng hiện tại không khớp với email của user trong shipment
        String currentUserPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Authorities: "+currentUserPhone );
        if (!shipment.getUser().getPhoneNumber().equals(currentUserPhone)) {
            throw new AccessDeniedException("Bạn không có quyền xóa đơn hàng này");
        }
        shipment.setStatus(ShipmentStatusEnum.CANCELED);  // Cập nhật trạng thái
        shipmentRepository.save(shipment);
    }
    @PostAuthorize("returnObject.userId == authentication.token.claims['userId']")
    public ShipmentResponse updateShipment(ShipmentRequest request, Long shipmentId) {
        User user=null;
        if(request.getUserId()!=null){
            user=userRepository.findById(request.getUserId())
                    .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        }
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new AppException(ErrorCode.SHIPMENT_NOT_EXISTED));
        shipmentMapper.updateShipment(shipment, request);
        shipment.setUser(user);
        return shipmentMapper.toShipmentResponse(shipmentRepository.save(shipment));
    }

    public ResultPaginationDTO fetchAllShipments(Specification<Shipment> spec, Pageable pageable) {
        Page<Shipment> pageShipments = this.shipmentRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageShipments.getTotalPages());
        meta.setTotal(pageShipments.getTotalElements());

        result.setMeta(meta);

        List<ShipmentResponse> shipmentList = pageShipments.getContent()
                .stream()
                .map(shipmentMapper::toShipmentResponse)
                .collect(Collectors.toList());
        result.setResult(shipmentList);

        return result;
    }
}
