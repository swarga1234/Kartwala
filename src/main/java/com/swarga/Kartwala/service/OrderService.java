package com.swarga.Kartwala.service;

import com.swarga.Kartwala.payload.OrderDTO;
import jakarta.transaction.Transactional;


public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
