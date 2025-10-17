package com.swarga.Kartwala.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long orderId;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email must not be null")
    private String email;

    @NotNull(message = "Order items must not be null")
    @Size(min = 1, message = "Order must have at least one item")
    private List<OrderItemDTO> orderItems;

    @NotNull(message = "Order date must not be null")
    private LocalDate orderDate;

    private PaymentDTO paymentDTO;

    @NotNull(message = "Total amount must not be null")
    @Min(value = 0, message = "Total amount must be zero or positive")
    private Double totalAmount;

    @NotBlank(message = "Order status must not be Blank")
    private String orderStatus;

    @NotNull(message = "addressId must not be null!!")
    private Long addressId;
}
