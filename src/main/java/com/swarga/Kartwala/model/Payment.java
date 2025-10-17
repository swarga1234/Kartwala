package com.swarga.Kartwala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Order order;

    @NotBlank(message = "The pgPaymentId can't be blank!!")
    private String pgPaymentId;
    @NotBlank(message = "The Payment Method can't be blank!!")
    private String paymentMethod;
    @NotBlank(message = "Payment Gateway status can't be blank!!")
    private String pgStatus;
    @NotBlank(message = "Payment Gateway Response can't be be blank!!")
    private String pgResponseMessage;
    @NotBlank(message = "Payment gateway name should not be blank!!")
    @Size(min = 3, message = "Payment gateway name should contain at least 3 characters!!")
    private String pgName;

    public Payment(String paymentMethod, String pgPaymentId, String pgResponseMessage, String pgStatus, String pgName) {
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgResponseMessage = pgResponseMessage;
        this.pgStatus = pgStatus;
        this.pgName = pgName;
    }
}
