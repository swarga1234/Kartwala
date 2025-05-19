package com.swarga.Kartwala.controller;

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

import com.swarga.Kartwala.model.Category;
import com.swarga.Kartwala.payload.CategoryDTO;
import com.swarga.Kartwala.payload.CategoryResponse;
import com.swarga.Kartwala.service.CategoryService;

import jakarta.validation.Valid;

@RestController
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/api/public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories(){
		
		//Get all categories
		 CategoryResponse categoryResponse = categoryService.getAllCategories();
		 return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
	}
	
	@PostMapping("/api/admin/category")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
		
		//Create a new category
		CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
		return new ResponseEntity<CategoryDTO>(savedCategoryDTO, HttpStatus.CREATED);
	}
	@DeleteMapping("/api/admin/categories/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
		
		//Delete a category
		String status = categoryService.deleteCategory(categoryId);
		return new ResponseEntity<String>(status, HttpStatus.OK);
		
	}
	@PutMapping("/api/admin/categories/{categoryId}")
	public ResponseEntity<String> updateCategory(@Valid @RequestBody Category category,@PathVariable Long categoryId) {
		
		//Update a category
		Category updatedCategory = categoryService.updateCategory(category, categoryId);
		String status="Category with Id: "+updatedCategory.getCategoryId()+" updated successfully!!";
		return new ResponseEntity<String>(status, HttpStatus.OK);
		
	}
}
