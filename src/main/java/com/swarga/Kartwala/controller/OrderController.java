package com.swarga.Kartwala.controller;

import com.swarga.Kartwala.model.Order;
import com.swarga.Kartwala.payload.OrderDTO;
import com.swarga.Kartwala.payload.OrderRequestDTO;
import com.swarga.Kartwala.service.OrderService;
import com.swarga.Kartwala.utils.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/users/payment/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod,
                                                  @Valid @RequestBody OrderRequestDTO orderRequestDTO)
    {
        String emailId= authUtils.loggedInEmail();
        OrderDTO orderDTO = orderService.placeOrder(emailId, orderRequestDTO.getAddressId(), paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage());

        return new ResponseEntity<OrderDTO>(orderDTO, HttpStatus.CREATED);
    }
}
