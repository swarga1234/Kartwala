package com.swarga.Kartwala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    @NotBlank
    @Size(min = 3 , message = "The product name must have at least 3 characters!!")
    private String productName;
    private String image;

    @NotBlank
    @Size(min = 10, message = "The product description must have at least 10 characters")
    private String description;

    @NotNull(message = "Quantity is required!!")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price is required!!")
    @Positive(message = "Price must be greater than 0!!")
    private Double price;

    @NotNull(message = "Discount is required!!")
    @DecimalMin(value = "0.0",  message = "Discount must be  0 or greater than 0!!")
    @DecimalMax(value = "100.0", message = "Discount can't be greater than 100%")
    private Double discount;

    @NotNull(message = "SpecialPrice is required!!")
    @PositiveOrZero(message = "SpecialPrice must be  0 or greater than 0!!")
    private Double specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category must not be null!!")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
//    @NotNull(message = "The seller can't be null!!")
    private User user;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();
}
