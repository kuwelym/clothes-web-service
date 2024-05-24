package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.demo.models.OrderContact}
 */
@Value
@Builder
public class OrderContactDTO implements Serializable {
    Long id;
    @NotNull(message = "First name cannot be null")
    String firstName;
    @NotNull(message = "Last name cannot be null")
    String lastName;
    @Pattern(regexp = "^(0[0-9]{2} [0-9]{3} [0-9]{4})|([0-9]{2} [0-9]{3} [0-9]{4})$", message = "Invalid phone number")
    String phone;
}