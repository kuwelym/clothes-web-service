package com.example.demo.services.mappers;

import com.example.demo.dto.StorePickupDTO;
import com.example.demo.models.StorePickup;

public class StorePickupServiceMapper {
    public static StorePickupDTO toStorePickupDTO(StorePickup storePickup) {
        return StorePickupDTO.builder()
                .id(storePickup.getId())
                .store(storePickup.getStore())
                .pickupDate(storePickup.getPickupDate())
                .build();
    }
}
