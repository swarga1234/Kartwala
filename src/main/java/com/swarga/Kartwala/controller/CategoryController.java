package com.swarga.Kartwala.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.swarga.Kartwala.model.Category;
import com.swarga.Kartwala.service.CategoryService;

@RestController
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/api/public/categories")
	public ResponseEntity<List<Category>> getAllCategories(){
		
		//Get all categories
		 List<Category> categories = categoryService.getAllCategories();
		 return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
	}
	
	@PostMapping("/api/admin/category")
	public ResponseEntity<String> createCategory(@RequestBody Category category) {
		
		//Create a new category
		Long categoryId = categoryService.createCategory(category);
		return new ResponseEntity<String>("New category with Id: "+categoryId+" created!!", HttpStatus.CREATED);
	}
	@DeleteMapping("/api/admin/categories/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
		
		//Delete a category
		try {
			String status = categoryService.deleteCategory(categoryId);
			return new ResponseEntity<String>(status, HttpStatus.OK);
		}catch (ResponseStatusException e) {
			return new ResponseEntity<String>(e.getReason(), e.getStatusCode());
		}
		
	}
	@PutMapping("/api/admin/categories/{categoryId}")
	public ResponseEntity<String> updateCategory(@RequestBody Category category,@PathVariable Long categoryId) {
		
		//Update a category
		try {
			Category updatedCategory = categoryService.updateCategory(category, categoryId);
			String status="Category with Id: "+updatedCategory.getCategoryId()+" updated successfully!!";
			return new ResponseEntity<String>(status, HttpStatus.OK);
		}catch (ResponseStatusException e) {
			return new ResponseEntity<String>(e.getReason(), e.getStatusCode());
		}
		
	}
}
