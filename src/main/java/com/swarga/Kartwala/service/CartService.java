package com.swarga.Kartwala.service;

import com.swarga.Kartwala.payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    @Transactional
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String email, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    @Transactional
    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCart(Long cartId, Long productId);
}
