package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "product_quantity",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_product_size_color",
                        columnNames = {"product_id", "size_id", "color_id"}
                )
        }
)
public class ProductQuantity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "color_id", referencedColumnName = "id", nullable = false)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "size_id", referencedColumnName = "id", nullable = false)
    private Size size;

    @Column(nullable = false)
    private int quantity;
}
