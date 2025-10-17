package com.swarga.Kartwala.repository;

import com.swarga.Kartwala.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci from CartItem ci WHERE ci.cart.cartId=?2 AND ci.product.productId=?1")
    CartItem findByProductIdAndCartId(Long productId, Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.product.productId=?1 AND ci.cart.cartId=?2")
    void deleteCartItemByProductIdAndCartId(Long productId, Long cartId);
}

