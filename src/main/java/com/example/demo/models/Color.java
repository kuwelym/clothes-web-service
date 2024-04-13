package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "colors",uniqueConstraints = {@UniqueConstraint(columnNames = {"hexCode", "product_id"})})
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hexCode;
    private String name;
}
