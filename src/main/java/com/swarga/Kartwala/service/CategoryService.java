package com.swarga.Kartwala.service;

import com.swarga.Kartwala.payload.CategoryDTO;
import com.swarga.Kartwala.payload.CategoryResponse;

public interface CategoryService {
	
	CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	CategoryDTO createCategory(CategoryDTO categoryDTO);
	CategoryDTO deleteCategory(Long categoryId);
	CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
