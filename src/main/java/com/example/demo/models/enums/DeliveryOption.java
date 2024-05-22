package com.example.demo.models.enums;

import com.example.demo.util.EnumUtils;

public enum DeliveryOption {
    ON_STORE_PICKUP, DELIVERY;
    @Override
    public String toString() {
        // Replace underscores with spaces and capitalize each word
        return EnumUtils.toReadableString(this);
    }
}
