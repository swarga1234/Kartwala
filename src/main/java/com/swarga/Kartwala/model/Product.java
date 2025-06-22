package com.swarga.Kartwala.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "product_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
