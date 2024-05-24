package com.example.demo.services.mappers;

import com.example.demo.dto.OrderDTO;
import com.example.demo.models.Order;

import java.util.stream.Collectors;

public class OrderServiceMapper {
    public static OrderDTO mapToOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderItems(order.getOrderItems().stream().map(OrderItemServiceMapper::toOrderItemDTO).collect(Collectors.toSet()))
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .orderClass(order.getOrderClass())
                .storePickup(order.getStorePickup() != null ? StorePickupServiceMapper.toStorePickupDTO(order.getStorePickup()) : null)
                .delivery(order.getDelivery() != null ? DeliveryServiceMapper.toDeliveryDTO(order.getDelivery()) : null)
                .deliveryOption(order.getDeliveryOption())
                .orderContact(order.getOrderContact() != null ? OrderContactServiceMapper.toOrderContactDTO(order.getOrderContact()) : null)
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
