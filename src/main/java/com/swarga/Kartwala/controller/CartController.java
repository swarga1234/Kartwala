package com.swarga.Kartwala.controller;

import com.swarga.Kartwala.exception.APIException;
import com.swarga.Kartwala.model.Cart;
import com.swarga.Kartwala.payload.CartDTO;
import com.swarga.Kartwala.repository.CartRepository;
import com.swarga.Kartwala.service.CartService;
import com.swarga.Kartwala.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtils authUtils;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts(){
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS, HttpStatus.OK);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCart(){

        String email = authUtils.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        if(cart==null){
            throw new APIException("User with email: "+email+" has not carts!!");
        }
        Long cartId=cart.getCartId();
        CartDTO cartDTO = cartService.getCart(email, cartId);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateProductQuantityInCart(@PathVariable Long productId,
                                                               @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("add")?1:-1);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId){

        String statusMessage = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<>(statusMessage, HttpStatus.OK);
    }
}
