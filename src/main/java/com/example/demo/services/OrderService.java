package com.example.demo.services;

import com.example.demo.dto.OrderItemDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    ResponseEntity<?> getOrders(Long userId);
    ResponseEntity<?> getOrder(Long userId, Long orderId);
    ResponseEntity<?> createOrder(Long userId, List<OrderItemDTO> orderItemDTOS, String address, String classification, String deliveryOption);
    ResponseEntity<?> updateOrder(Long userId, Long orderId, String orderStatus, Boolean isAdmin);
    ResponseEntity<?> deleteOrder(Long userId, Long orderId);
    ResponseEntity<?> deleteAllOrders(Long userId);
}
