package com.swarga.Kartwala.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long cartItemId;
    private CartDTO cartDTO;
    private ProductDTO productDTO;

    @NotNull(message = "Quantity is required!!")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @NotNull(message = "Discount is required!!")
    @DecimalMin(value = "0.0",  message = "Discount must be  0 or greater than 0!!")
    @DecimalMax(value = "100.0", message = "Discount can't be greater than 100%")
    private Double discount;

    @NotNull(message = "Price is required!!")
    @Positive(message = "Price must be greater than 0!!")
    private Double productPrice;
}
