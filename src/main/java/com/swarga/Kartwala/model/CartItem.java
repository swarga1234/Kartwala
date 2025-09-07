package com.swarga.Kartwala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @NotNull(message = "Discount is required!!")
    @DecimalMin(value = "0.0",  message = "Discount must be  0 or greater than 0!!")
    @DecimalMax(value = "100.0", message = "Discount can't be greater than 100%")
    @Column(name = "discount")
    private Double discount;

    @NotNull(message = "Price is required!!")
    @Positive(message = "Price must be greater than 0!!")
    @Column(name = "product_price")
    private Double productPrice;

    @NotNull(message = "Quantity is required!!")
    @Min(value = 0, message = "Quantity must be at least 0")
    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
