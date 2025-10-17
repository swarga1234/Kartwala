package com.swarga.Kartwala.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long oderItemId;

    @NotNull(message = "Product must not be null")
    private ProductDTO productDTO;

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
