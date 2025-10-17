package com.swarga.Kartwala.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long paymentId;
    @NotBlank(message = "The Payment Method can't be blank!!")
    private String paymentMethod;
    @NotBlank(message = "Payment Gateway status can't be blank!!")
    private String pgStatus;
    @NotBlank(message = "Payment Gateway Response can't be be blank!!")
    private String pgResponseMessage;
    @NotBlank(message = "Payment gateway name should not be blank!!")
    @Size(min = 3, message = "Payment gateway name should contain at least 3 characters!!")
    private String pgName;

}
