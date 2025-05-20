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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swarga.Kartwala.config.AppConstants;
import com.swarga.Kartwala.payload.CategoryDTO;
import com.swarga.Kartwala.payload.CategoryResponse;
import com.swarga.Kartwala.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
//	@GetMapping("/echo")
//	public ResponseEntity<String> echoMessage(@RequestParam(name = "message") String message){
//		return new ResponseEntity<String>(message, HttpStatus.OK);
//	}
	
	@GetMapping("/public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber"
	, defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber, 
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_CATEGORY_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortOrder){
		
		//Get all categories
		 CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
		 return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
	}
	
	@PostMapping("/admin/category")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
		
		//Create a new category
		CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
		return new ResponseEntity<CategoryDTO>(savedCategoryDTO, HttpStatus.CREATED);
	}
	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
		
		//Delete a category
		CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
		
	}
	@PutMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId) {
		
		//Update a category
		CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
		//String status="Category with Id: "+updatedCategory.getCategoryId()+" updated successfully!!";
		return new ResponseEntity<CategoryDTO>(updatedCategoryDTO, HttpStatus.OK);
		
	}
}
