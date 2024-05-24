package com.example.demo.dto;

import com.example.demo.models.enums.DeliveryOption;
import com.example.demo.models.enums.OrderClass;
import com.example.demo.models.enums.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.example.demo.models.Order}
 */
@Value
@Builder
public class OrderDTO implements Serializable {
    Long id;
    Set<OrderItemDTO> orderItems;
    LocalDateTime orderDate;
    OrderStatus orderStatus;
    OrderClass orderClass;
    StorePickupDTO storePickup;
    DeliveryDTO delivery;
    DeliveryOption deliveryOption;
    OrderContactDTO orderContact;
    double totalPrice;

}