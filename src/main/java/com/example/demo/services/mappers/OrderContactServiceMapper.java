package com.example.demo.services.mappers;

import com.example.demo.dto.OrderContactDTO;
import com.example.demo.models.OrderContact;

public class OrderContactServiceMapper {
    public static OrderContact toOrderContact(OrderContactDTO orderContactDTO) {
        return OrderContact.builder()
                .id(orderContactDTO.getId())
                .firstName(orderContactDTO.getFirstName())
                .lastName(orderContactDTO.getLastName())
                .phone(orderContactDTO.getPhone())
                .build();
    }

    public static OrderContactDTO toOrderContactDTO(OrderContact orderContact) {
        return OrderContactDTO.builder()
                .id(orderContact.getId())
                .firstName(orderContact.getFirstName())
                .lastName(orderContact.getLastName())
                .phone(orderContact.getPhone())
                .build();
    }
}
