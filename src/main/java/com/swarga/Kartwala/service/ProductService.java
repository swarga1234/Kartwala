package com.swarga.Kartwala.service;

import com.swarga.Kartwala.payload.ProductDTO;
import com.swarga.Kartwala.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);
}
