package com.swarga.Kartwala.service;

import java.util.List;

import com.swarga.Kartwala.model.Category;

public interface CategoryService {
	
	List<Category> getAllCategories();
	Long createCategory(Category category);
	String deleteCategory(Long categoryId);
	Category updateCategory(Category category, Long categoryId);
}
