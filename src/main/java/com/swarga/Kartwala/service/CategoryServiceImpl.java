package com.swarga.Kartwala.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swarga.Kartwala.exception.APIException;
import com.swarga.Kartwala.exception.ResourceNotFoundException;
import com.swarga.Kartwala.model.Category;
import com.swarga.Kartwala.payload.CategoryDTO;
import com.swarga.Kartwala.payload.CategoryResponse;
import com.swarga.Kartwala.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CategoryResponse getAllCategories() {
		// Get all the available categories
		 List<Category> categories = categoryRepository.findAll();
		 if(categories==null || categories.isEmpty()) {
			 throw new APIException("No Categories are available!!");
		 }
		 List<CategoryDTO> categoryDTOs= categories.stream()
				 						.map(category -> modelMapper.map(category, CategoryDTO.class))
				 						.toList();
		 CategoryResponse categoryResponse= new CategoryResponse();
		 categoryResponse.setContent(categoryDTOs);
		return categoryResponse;
	}

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		// Add a new category
		Optional<Category> existingCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
		if(existingCategory.isPresent()) {
			throw new APIException("The Category with name: "+existingCategory.get().getCategoryName()+" already exists!!");
		}
		Category category = modelMapper.map(categoryDTO, Category.class);
		Category savedCategory = categoryRepository.save(category);
		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	@Override
	public String deleteCategory(Long categoryId) {
		Category savedCategory = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));
		
		categoryRepository.delete(savedCategory);
		return "Category with Id: "+categoryId+" deleted!!";
	}

	@Override
	public Category updateCategory(Category category, Long categoryId) {
		// Update a category
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));
		
		existingCategory.setCategoryName(category.getCategoryName());
		return categoryRepository.save(existingCategory);
	}

}
