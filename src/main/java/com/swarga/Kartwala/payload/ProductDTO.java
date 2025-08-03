package com.swarga.Kartwala.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long productId;

    @NotBlank
    @Size(min = 3 , message = "The product name must have at least 3 characters!!")
    private String productName;

    @NotBlank
    @Size(min = 10, message = "The product description must have at least 10 characters")
    private String description;

    private String image;

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

    private Double specialPrice;

}
