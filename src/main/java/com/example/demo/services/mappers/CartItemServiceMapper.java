package com.example.demo.services.mappers;

import com.example.demo.dto.CartItemDTO;
import com.example.demo.models.CartItem;
import com.example.demo.models.Color;
import com.example.demo.models.Product;
import com.example.demo.models.Size;

public class CartItemServiceMapper {
    public static CartItemDTO toCartItemDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .selectedColorId(cartItem.getSelectedColor().getId())
                .selectedSizeId(cartItem.getSelectedSize().getId())
                .productId(cartItem.getProduct().getId())
                .userId(cartItem.getUser().getId())
                .build();
    }

    public static CartItem toCartItem(CartItemDTO cartItemDTO) {
        return CartItem.builder()
                .id(cartItemDTO.getId())
                .quantity(cartItemDTO.getQuantity())
                .totalPrice(cartItemDTO.getTotalPrice())
                .selectedColor(Color.builder().id(cartItemDTO.getSelectedColorId()).build())
                .selectedSize(Size.builder().id(cartItemDTO.getSelectedSizeId()).build())
                .product(Product.builder().id(cartItemDTO.getProductId()).build())
                .build();
    }
}
