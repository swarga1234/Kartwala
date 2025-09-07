package com.swarga.Kartwala.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long cartId;

    @NotNull
    @Positive(message = "The total price must be greater than 0!")
    private Double totalPrice;

    private List<ProductDTO> products = new ArrayList<>();
}
