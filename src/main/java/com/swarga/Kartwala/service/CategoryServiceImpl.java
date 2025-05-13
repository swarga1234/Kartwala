package com.swarga.Kartwala.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.swarga.Kartwala.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService{

	private List<Category> categoryList=new ArrayList<>();
	private long nextId=1L;
	
	@Override
	public List<Category> getAllCategories() {
		// Get all the available categories
		return categoryList;
	}

	@Override
	public Long createCategory(Category category) {
		// Add a new category
		category.setCategoryId(nextId++);
		categoryList.add(category);
		return category.getCategoryId();
	}

	@Override
	public String deleteCategory(Long categoryId) {
		Category category= categoryList.stream()
						.filter(c -> c.getCategoryId().equals(categoryId))
						.findFirst()
						.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with Id: "
								+categoryId+" not found!!"));
		categoryList.remove(category);
		return "Category with Id: "+categoryId+" deleted!!";
	}

	@Override
	public Category updateCategory(Category category, Long categoryId) {
		// Update a category
		Category existingCategory= categoryList.stream()
								.filter(c -> c.getCategoryId().equals(categoryId))
								.findFirst()
								.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with Id: "
										+categoryId+" not found!!"));
		existingCategory.setCategoryName(category.getCategoryName());
		return existingCategory;
	}

}
