package com.swarga.Kartwala.controller;

import com.swarga.Kartwala.config.AppConstants;
import com.swarga.Kartwala.model.Product;
import com.swarga.Kartwala.payload.ProductDTO;
import com.swarga.Kartwala.payload.ProductResponse;
import com.swarga.Kartwala.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    //API to add a product
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO, @PathVariable Long categoryId){
        ProductDTO savedProductDTO = productService.addProduct(categoryId, productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    //API to fetch all products
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortOrder){

        //Get all products
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    //API to fetch all products in a category
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_PRODUCT_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortOrder)
    {
        ProductResponse productResponse = productService.getProductsByCategory(categoryId,pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    //API to search all products using a keyword
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getAllProductsByKeyword( @PathVariable String keyword,
    @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                    @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                    @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_PRODUCT_BY, required = false) String sortBy,
                                                                    @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortOrder){

        ProductResponse productResponse = productService.searchProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }

    //API to update a Product
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long productId){

        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    //API for deleting a product
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deletedProductDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDTO,HttpStatus.OK);
    }
}
