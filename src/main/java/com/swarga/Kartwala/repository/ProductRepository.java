package com.swarga.Kartwala.repository;

import com.swarga.Kartwala.model.Category;
import com.swarga.Kartwala.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategory(Category category, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
