package com.example.demo.services.impl;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.models.*;
import com.example.demo.models.enums.DeliveryOption;
import com.example.demo.models.enums.OrderClass;
import com.example.demo.models.enums.OrderStatus;
import com.example.demo.repository.*;
import com.example.demo.services.OrderService;
import com.example.demo.services.mappers.OrderServiceMapper;
import com.example.demo.util.EnumUtils;
import com.example.demo.util.ValidationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ValidationUtils ValidationUtils;
    private final ProductQuantityRepository productQuantityRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ValidationUtils validationUtils, ProductQuantityRepository productQuantityRepository, ProductRepository productRepository, ColorRepository colorRepository, SizeRepository sizeRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        ValidationUtils = validationUtils;
        this.productQuantityRepository = productQuantityRepository;
        this.productRepository = productRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
    }

    @Override
    public ResponseEntity<?> getOrders(Long userId) {
        return validateUserAndExecute(userId, () -> {
            List<Order> orders = orderRepository.findAllByUserId(userId);
            List<OrderDTO> orderDTOS = orders.stream().map(OrderServiceMapper::mapToOrderDTO).collect(Collectors.toList());
            return ResponseEntity.ok(orderDTOS);
        });
    }

    @Override
    public ResponseEntity<?> getOrder(Long userId, Long orderId) {
        return validateUserAndExecute(userId, () -> {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null) {
                return ResponseEntity.badRequest().body("Order not found");
            }
            return ResponseEntity.ok(OrderServiceMapper.mapToOrderDTO(order));
        });
    }

    @Override
    public ResponseEntity<?> createOrder(Long userId, List<OrderItemDTO> orderItemDTOS, String address, String classification, String deliveryOption) {
        return validateUserAndExecute(userId, () -> {
            for (OrderItemDTO orderItemDTO : orderItemDTOS) {
                ProductQuantity productQuantity = productQuantityRepository.findByProductIdAndColorIdAndSizeId(orderItemDTO.getProductId(), orderItemDTO.getColorId(), orderItemDTO.getSizeId());
                if (productQuantity == null || !isEnoughProductAvailable(productQuantity, orderItemDTO.getQuantity())) {
                    return ResponseEntity.badRequest().body("Not enough products available");
                }
            }

            Order order = new Order();
            order.setUser((User) ValidationUtils.validateUserExistence(userId).getBody());
            order.setAddress(address);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setOrderClass(EnumUtils.getEnumFromString(OrderClass.class, classification));
            order.setDeliveryOption(DeliveryOption.valueOf(deliveryOption));
            order.setTotalPrice(orderItemDTOS.stream().mapToDouble(OrderItemDTO::getTotalPrice).sum());

            orderRepository.save(order);

            Set<OrderItem> orderItems = orderItemDTOS.stream().map(itemDTO -> {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                Color color = colorRepository.findById(itemDTO.getColorId())
                        .orElseThrow(() -> new RuntimeException("Color not found"));
                Size size = sizeRepository.findById(itemDTO.getSizeId())
                        .orElseThrow(() -> new RuntimeException("Size not found"));

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setSelectedColor(color);
                orderItem.setSelectedSize(size);
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setTotalPrice(itemDTO.getTotalPrice());
                orderItem.setOrder(order);

                return orderItem;
            }).collect(Collectors.toSet());

            orderItemRepository.saveAll(orderItems);
            order.setOrderItems(orderItems);
            orderRepository.save(order);

            updateProductQuantities(orderItemDTOS);

            return ResponseEntity.ok(OrderServiceMapper.mapToOrderDTO(order));
        });
    }

    // Admin update order status to either CONFIRMED, DELIVERED, CANCELLED, AVAILABLE_FOR_PICKUP, PICKED_UP, ON_THE_WAY
    // User update order status to CANCELLED, CONFIRMED, DELIVERED, PICKED_UP
    @Override
    public ResponseEntity<?> updateOrder(Long userId, Long orderId, String orderStatus,Boolean isAdmin) {
        return validateUserAndExecute(userId, () -> {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null) {
                return ResponseEntity.badRequest().body("Order not found");
            }

            OrderStatus orderStatusEnum = EnumUtils.getEnumFromString(OrderStatus.class, orderStatus);

            if (!hasPermissionToUpdateStatus(isAdmin, orderStatusEnum)) {
                return ResponseEntity.badRequest().body("Unauthorized status update");
            }

            if (orderStatusEnum == OrderStatus.CANCELLED) {
                if (!canCancelOrder(order)) {
                    return ResponseEntity.badRequest().body("Order can only be cancelled if it is pending and within 30 minutes of creation");
                }
                restoreProductQuantities(order);
            }

            order.setOrderStatus(orderStatusEnum);
            orderRepository.save(order);

            return ResponseEntity.ok(OrderServiceMapper.mapToOrderDTO(order));
        });
    }

    @Override
    public ResponseEntity<?> deleteOrder(Long userId, Long orderId) {
        return validateUserAndExecute(userId, () -> {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null) {
                return ResponseEntity.badRequest().body("Order not found");
            }
            orderRepository.delete(order);
            return ResponseEntity.ok("Order deleted");
        });
    }

    @Override
    public ResponseEntity<?> deleteAllOrders(Long userId) {
        return validateUserAndExecute(userId, () -> {
            orderRepository.deleteAllByUserId(userId);
            return ResponseEntity.ok("All orders deleted");
        });
    }

    private boolean isEnoughProductAvailable(ProductQuantity productQuantity, int quantity) {
        return productQuantity.getQuantity() >= quantity;
    }

    private void updateProductQuantities(List<OrderItemDTO> orderItemDTOS) {
        orderItemDTOS.forEach(orderItemDTO -> {
            ProductQuantity productQuantity = productQuantityRepository.findByProductIdAndColorIdAndSizeId(orderItemDTO.getProductId(), orderItemDTO.getColorId(), orderItemDTO.getSizeId());
            productQuantity.setQuantity(productQuantity.getQuantity() - orderItemDTO.getQuantity());
            productQuantityRepository.save(productQuantity);
        });
    }

    private void restoreProductQuantities(Order order) {
        order.getOrderItems().forEach(orderItem -> {
            ProductQuantity productQuantity = productQuantityRepository.findByProductIdAndColorIdAndSizeId(orderItem.getProduct().getId(), orderItem.getSelectedColor().getId(), orderItem.getSelectedSize().getId());
            productQuantity.setQuantity(productQuantity.getQuantity() + orderItem.getQuantity());
            productQuantityRepository.save(productQuantity);
        });
    }

    private boolean canCancelOrder(Order order) {
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            return false;
        }
        LocalDateTime cancelDeadline = order.getOrderDate().plusMinutes(30);
        return LocalDateTime.now().isBefore(cancelDeadline);
    }

    private boolean hasPermissionToUpdateStatus(Boolean isAdmin, OrderStatus orderStatus) {
        List<OrderStatus> validUserStatuses = Arrays.asList(OrderStatus.CANCELLED, OrderStatus.CONFIRMED, OrderStatus.DELIVERED, OrderStatus.PICKED_UP);
        List<OrderStatus> validAdminStatuses = Arrays.asList(OrderStatus.CONFIRMED, OrderStatus.DELIVERED, OrderStatus.CANCELLED, OrderStatus.AVAILABLE_FOR_PICKUP, OrderStatus.PICKED_UP, OrderStatus.ON_THE_WAY);

        if (isAdmin) {
            return validAdminStatuses.contains(orderStatus);
        } else {
            return validUserStatuses.contains(orderStatus);
        }
    }

    private ResponseEntity<?> validateUserAndExecute(Long userId, Action action) {
        ResponseEntity<?> userExistenceError = ValidationUtils.validateUserExistence(userId);
        if (userExistenceError == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return action.execute();
    }

    @FunctionalInterface
    private interface Action {
        ResponseEntity<?> execute();
    }
}
