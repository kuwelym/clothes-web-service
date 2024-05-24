package com.example.demo.services;

import com.example.demo.dto.DeliveryDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.StorePickupDTO;
import com.example.demo.models.enums.DeliveryOption;
import com.example.demo.models.enums.OrderClass;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Set;

public interface OrderService {
    ResponseEntity<?> getOrders(Long userId);
    ResponseEntity<?> getOrder(Long userId, Long orderId);
    ResponseEntity<?> createOrder(Long userId, Set<OrderItemDTO> orderItemDTOS, OrderClass classification, DeliveryOption deliveryOption, DeliveryDTO deliveryDTO, StorePickupDTO storePickupDTO);
    ResponseEntity<?> updateOrder(Long userId, Long orderId, String orderStatus, LocalDate deliveryDate, Boolean isAdmin);
    ResponseEntity<?> deleteOrder(Long userId, Long orderId);
    ResponseEntity<?> deleteAllOrders(Long userId);
}
