package com.swarga.Kartwala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oderItemId;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @NotNull(message = "Product must not be null")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @NotNull(message = "Order must not be null")
    private Order order;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Discount must not be null")
    @Min(value = 0, message = "Discount must be zero or positive")
    private Double discount;

    @NotNull(message = "Ordered product price must not be null")
    @Positive(message = "Ordered product price must be positive")
    private Double orderedProductPrice;
}
