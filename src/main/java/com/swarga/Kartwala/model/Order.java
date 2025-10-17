package com.swarga.Kartwala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email must not be null")
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @NotNull(message = "Order items must not be null")
    @Size(min = 1, message = "Order must have at least one item")
    private List<OrderItem> orderItems = new ArrayList<>();

    @NotNull(message = "Order date must not be null")
    private LocalDate orderDate;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @NotNull(message = "Total amount must not be null")
    @Min(value = 0, message = "Total amount must be zero or positive")
    private Double totalAmount;

    @NotBlank(message = "Order status must not be Blank")
    private String orderStatus;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @NotNull(message = "Address must not be null!!")
    private Address address;


}

