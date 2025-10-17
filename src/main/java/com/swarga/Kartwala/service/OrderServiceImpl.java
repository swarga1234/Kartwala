package com.swarga.Kartwala.service;

import com.swarga.Kartwala.exception.APIException;
import com.swarga.Kartwala.exception.ResourceNotFoundException;
import com.swarga.Kartwala.model.*;
import com.swarga.Kartwala.payload.OrderDTO;
import com.swarga.Kartwala.payload.OrderItemDTO;
import com.swarga.Kartwala.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {

        //Getting User cart
        Cart cart = cartRepository.findCartByEmail(emailId);
        if(cart==null)
        {
            throw new ResourceNotFoundException("Cart","email",emailId);
        }

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("address","addressId",addressId));
        //Create a new order with payment info

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted!!");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod,pgPaymentId, pgResponseMessage, pgStatus, pgName);
        payment.setOrder(order);
        Payment savedPayment = paymentRepository.save(payment);

        order.setPayment(savedPayment);
        Order savedOrder = orderRepository.save(order);
        //Get items from the cart into the order items
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()){
            throw new APIException("The Cart is Empty!!");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem: cartItems){

            OrderItem orderItem = new OrderItem();
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        //Update product stock
        cartItems.forEach(item -> {
            int quantity = item.getQuantity();
            Product product= item.getProduct();
            product.setQuantity(product.getQuantity()-quantity);
            productRepository.save(product);
            //Clear product from cart
            cartService.deleteProductFromCart(cart.getCartId(), product.getProductId());
        });
        //Send order summary
        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(orderItem -> {
            orderDTO.getOrderItems().add(modelMapper.map(orderItem, OrderItemDTO.class));
        });
        orderDTO.setAddressId(addressId);
        return orderDTO;
    }
}
