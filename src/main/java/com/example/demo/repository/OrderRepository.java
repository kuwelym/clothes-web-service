package com.example.demo.repository;

import com.example.demo.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderRepository is a JPA Repository for {@link Order} entities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Find all orders by user id.
     *
     * @param userId the user id
     * @return the list of orders
     */
    List<Order> findAllByUserId(Long userId);

    /**
     * Delete all orders by user id.
     *
     * @param userId the user id
     */
    void deleteAllByUserId(Long userId);
}