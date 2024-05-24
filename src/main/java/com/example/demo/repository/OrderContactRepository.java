package com.example.demo.repository;

import com.example.demo.models.OrderContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderContactRepository extends JpaRepository<OrderContact, Long> {
}