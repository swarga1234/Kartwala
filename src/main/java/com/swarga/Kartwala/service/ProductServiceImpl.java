package com.swarga.Kartwala.service;

import com.swarga.Kartwala.config.AppConstants;
import com.swarga.Kartwala.exception.APIException;
import com.swarga.Kartwala.exception.ResourceNotFoundException;
import com.swarga.Kartwala.model.Cart;
import com.swarga.Kartwala.model.Category;
import com.swarga.Kartwala.model.Product;
import com.swarga.Kartwala.payload.CartDTO;
import com.swarga.Kartwala.payload.ProductDTO;
import com.swarga.Kartwala.payload.ProductResponse;
import com.swarga.Kartwala.repository.CartRepository;
import com.swarga.Kartwala.repository.CategoryRepository;
import com.swarga.Kartwala.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private CartService cartService;

    @Autowired
    private EntityManager entityManager;

    @Value("${project.product.image}")
    private String productImageDir;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow( () -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage(AppConstants.DEFAULT_PRODUCT_IMAGE);
        product.setCategory(category);
        product.setSpecialPrice(product.getPrice()- (product.getDiscount()/100)*product.getPrice());
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByandOrder = sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> products = productPage.getContent();
        if( products==null || products.isEmpty()){
            throw new APIException("No Products available!!");
        }
        List<ProductDTO> productDTOS = products.stream()
                .map( product -> modelMapper.map(product, ProductDTO.class) )
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow( () -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Sort sortByandOrder = sortOrder.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Product> productPage = productRepository.findByCategory(existingCategory, pageDetails);
        List<Product> products = productPage.getContent();
        if(products==null || products.isEmpty()){
            throw new APIException("No Products available for category: "+existingCategory.getCategoryName());
        }
        List<ProductDTO> productDTOS =products.stream()
                .map( product -> {
                    return modelMapper.map(product, ProductDTO.class);
                }).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByandOrder= sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByandOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%", pageDetails);
        List<Product> products = productPage.getContent();
        if(products==null || products.isEmpty()){
            throw new APIException("No matching Products with keyword: "+keyword+" found!!");
        }
        List<ProductDTO> productDTOS = products.stream().map( product -> {
            return modelMapper.map(product, ProductDTO.class);
        }).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product", "productId", productId));
        //Product product = modelMapper.map(productDTO, Product.class);
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setDiscount(productDTO.getDiscount());
        existingProduct.setPrice(productDTO.getPrice());
        double specialPrice = productDTO.getPrice() - (productDTO.getDiscount()/100)* productDTO.getPrice();
        existingProduct.setSpecialPrice(specialPrice);
        Product updatedProduct = productRepository.save(existingProduct);
        //When product is updated, it should also reflect in the products added to the user carts.
        // Like if there is a increase in price of the product, so if the product is added to the user's cart,
        // there also this increased price should be reflected.

        //Fetch all the carts where this product is added
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        //Convert it to DTO
        List<CartDTO> cartDTOs = carts.stream().map(
                cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<ProductDTO> productDTOS = cart.getCartItems().stream().map(
                            item -> {
                                ProductDTO pDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                                pDTO.setQuantity(item.getQuantity());
                                return pDTO;
                            }
                    ).toList();
                    cartDTO.setProducts(productDTOS);
                    return cartDTO;
                }
        ).toList();
        cartDTOs.forEach(cartDTO -> cartService.updateProductInCart(cartDTO.getCartId(), productId));
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Transactional
    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product", "productId", productId));
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> {
            cartService.deleteProductFromCart(cart.getCartId(), productId);
        });
//        entityManager.flush();
        productRepository.deleteById(existingProduct.getProductId());
        return modelMapper.map(existingProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile productImage) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product", "productId", productId));
        String filename = fileService.uploadImage(productImageDir, productImage); //Update image to server
        existingProduct.setImage(filename);
        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

}
