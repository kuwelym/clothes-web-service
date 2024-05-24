package com.example.demo.services.mappers;

import com.example.demo.dto.DeliveryDTO;
import com.example.demo.models.Delivery;

public class DeliveryServiceMapper {
    public static DeliveryDTO toDeliveryDTO(Delivery delivery) {
        return DeliveryDTO.builder()
                .id(delivery.getId())
                .deliveryDate(delivery.getDeliveryDate())
                .address(delivery.getAddress())
                .build();
    }
}
