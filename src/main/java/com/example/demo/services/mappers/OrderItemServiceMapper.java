package com.example.demo.services.mappers;

import com.example.demo.dto.OrderItemDTO;
import com.example.demo.models.*;

public class OrderItemServiceMapper {

    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .selectedColorId(orderItem.getSelectedColor().getId())
                .selectedSizeId(orderItem.getSelectedSize().getId())
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }

}
