package com.swarga.Kartwala.service;

import com.swarga.Kartwala.model.Category;
import com.swarga.Kartwala.payload.CategoryDTO;
import com.swarga.Kartwala.payload.CategoryResponse;

public interface CategoryService {
	
	CategoryResponse getAllCategories();
	CategoryDTO createCategory(CategoryDTO categoryDTO);
	String deleteCategory(Long categoryId);
	Category updateCategory(Category category, Long categoryId);
}
