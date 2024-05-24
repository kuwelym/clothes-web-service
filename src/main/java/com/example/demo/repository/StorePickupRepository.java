package com.example.demo.repository;

import com.example.demo.models.StorePickup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePickupRepository extends JpaRepository<StorePickup, Long> {
}