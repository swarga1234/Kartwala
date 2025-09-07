package com.swarga.Kartwala.service;

import com.swarga.Kartwala.exception.APIException;
import com.swarga.Kartwala.exception.ResourceNotFoundException;
import com.swarga.Kartwala.model.Cart;
import com.swarga.Kartwala.model.CartItem;
import com.swarga.Kartwala.model.Product;
import com.swarga.Kartwala.payload.CartDTO;
import com.swarga.Kartwala.payload.ProductDTO;
import com.swarga.Kartwala.repository.CartItemRepository;
import com.swarga.Kartwala.repository.CartRepository;
import com.swarga.Kartwala.repository.ProductRepository;
import com.swarga.Kartwala.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        //Find existing card or create one
        Cart cart = createCart();
        Product product = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product","productId",productId));
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(cart.getCartId(), productId);
        if(cartItem!=null){
            throw new APIException("Product "+product.getProductName()+" is already added to the cart!!");
        }
        if(product.getQuantity()==0){
            throw  new APIException(product.getProductName()+" is out of stock!!");
        }
        if(product.getQuantity()<quantity){
            throw new APIException("Please make an order of the product "+product.getProductName()+
                    " less than or equal to the quantity "+product.getQuantity()+".");
        }
        //Create cart Item
        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setDiscount(product.getDiscount());
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setQuantity(quantity);
        //save cart item
        cartItemRepository.save(cartItem);

        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        //System.out.println(cartItems);
        List<ProductDTO> productDTOS = cartItems.stream().map( item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(quantity);
            return productDTO;
        }).toList();
        cartDTO.setProducts(productDTOS);
        //return updated cart
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
       List<Cart> carts = cartRepository.findAll();
       List<CartDTO>  cartDTOS = carts.stream().map( cart -> {
           CartDTO cartDTO =modelMapper.map(cart, CartDTO.class);
           List<ProductDTO> productDTOS = cart.getCartItems().stream().map(
                   item -> {
                       ProductDTO productDTO= modelMapper.map(item.getProduct(), ProductDTO.class);
                       productDTO.setQuantity(item.getQuantity());
                       return productDTO;
                   }
           ).toList();
           cartDTO.setProducts(productDTOS);
           return cartDTO;
       }).toList();

       return cartDTOS;
    }

    @Override
    public CartDTO getCart(String email, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(email, cartId);
        if(cart==null){
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> products = cart.getCartItems().stream().map(
                item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    //System.out.println(item.getQuantity());
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                }
        ).toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String email = authUtils.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        if(cart==null){
            throw new APIException("The user has no cart!!");
        }
        Long cartId = cart.getCartId();
        Cart userCart = cartRepository.findById(cartId).
                orElseThrow( () -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product","productId",productId));

        if(product.getQuantity()==0){
            throw  new APIException(product.getProductName()+" is out of stock!!");
        }
        if(product.getQuantity()<quantity){
            throw new APIException("Please make an order of the product "+product.getProductName()+
                    " less than or equal to the quantity "+product.getQuantity()+".");
        }

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(cart.getCartId(), productId);
        if(cartItem==null){
            throw new APIException("Product "+product.getProductName()+" is not added to the cart!!");
        }
        int newQuantity = cartItem.getQuantity()+quantity;
        if(newQuantity<0){
            throw new APIException("The CartItem Quantity can't be negative!!");
        }
        if(newQuantity==0){
            deleteProductFromCart(cartId,productId);
        }else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
            cartItem.setDiscount(product.getDiscount());
            cartItemRepository.save(cartItem);
            cart.setTotalPrice(cart.getTotalPrice()+ (cartItem.getProductPrice()*quantity));
            cartRepository.save(cart);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> products = cart.getCartItems().stream().map(
                item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    //System.out.println(item.getQuantity());
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                }
        ).toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart","cartId",cartId)
        );
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId,cartId);
        if(cartItem==null){
            throw new ResourceNotFoundException("Product","productId",productId);
        }
        cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity()));
        cart.getCartItems().remove(cartItem);
        cartItemRepository.deleteCartItemByProductIdAndCartId(productId,cartId);
        //System.out.println("Product: "+cartItem.getProduct().getProductName());
        cartRepository.save(cart);
        return "The Product: "+cartItem.getProduct().getProductName()+" is removed from cart!!";
    }

    @Override
    public void updateProductInCart(Long cartId, Long productId) {

        System.out.println("CartId: "+cartId +" productId: "+productId);
        Cart cart = cartRepository.findById(cartId).
                orElseThrow( () -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product","productId",productId));

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(productId,cartId);
        if(cartItem==null){
            throw new APIException("Product "+product.getProductName()+" is not available in the cart!!");
        }

        double cartTotalPrice= cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cartTotalPrice = cartTotalPrice+ cartItem.getProductPrice()*cartItem.getQuantity();
        cart.setTotalPrice(cartTotalPrice);

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

    }

    private Cart createCart(){
        Cart cart = cartRepository.findCartByEmail(authUtils.loggedInEmail());
        if(cart!=null){
            return cart;
        }
        cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtils.loggedInUser());
        return cartRepository.save(cart);
    }
}
