package com.example.demo.repository;

import com.example.demo.models.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Long> {
//    @Query("""
//            SELECT CASE WHEN COUNT(pq) > 0 THEN TRUE ELSE FALSE END
//            FROM ProductQuantity pq
//            WHERE pq.product.id = :productId
//            AND pq.color.id = :colorId
//            AND pq.size.id = :sizeId
//            """
//    )
    boolean existsByProductIdAndColorIdAndSizeId(Long productId, Long colorId, Long sizeId);

    ProductQuantity findByProductIdAndColorIdAndSizeId(Long productId, Long colorId, Long sizeId);

    List<ProductQuantity> findAllByProductId(Long productId);

    void deleteAllByProductId(Long productId);
}
