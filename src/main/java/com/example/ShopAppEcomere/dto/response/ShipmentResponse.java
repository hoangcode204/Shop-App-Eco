package com.example.ShopAppEcomere.dto.response;

import com.example.ShopAppEcomere.entity.User;
import com.example.ShopAppEcomere.enums.ShipmentStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ShipmentResponse {

    private Long id;
    private String address;
    private String zipCode;
    private Date shipmentDate;
    private Long userId;
    private ShipmentStatusEnum status;


}
