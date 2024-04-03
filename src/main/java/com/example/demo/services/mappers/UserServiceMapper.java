package com.example.demo.services.mappers;

import com.example.demo.dto.UserDTO;
import com.example.demo.models.User;

public class UserServiceMapper {
    public static UserDTO toProductDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNumber())
                .build();
    }
}
