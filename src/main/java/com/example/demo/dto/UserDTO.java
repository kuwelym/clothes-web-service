package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.example.demo.models.User}
 */

@Builder
@Value
public class UserDTO {
    Long id;
    String username;
    String email;
    String phoneNum;
}
