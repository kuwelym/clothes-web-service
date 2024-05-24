package com.example.demo.models.enums;

import com.example.demo.util.EnumUtils;

public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    AVAILABLE_FOR_PICKUP("Available for Pickup"),
    PICKED_UP("Picked Up"),
    ON_THE_WAY("On the Way");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        // Replace underscores with spaces and capitalize each word
        return EnumUtils.toReadableString(this);
    }
}
