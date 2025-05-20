package com.swarga.Kartwala.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	public CategoryResponse getAllCategories(Integer pageNumber ,Integer pageSize, String sortBy, String sortOrder) {
		// Get all the available categories
		Sort sortByandOrder= sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		
		Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByandOrder);
		Page<Category> categoryPage= categoryRepository.findAll(pageDetails);
		List<Category> categories = categoryPage.getContent();
		if(categories==null || categories.isEmpty()) {
			throw new APIException("No Categories are available!!");
		}
		List<CategoryDTO> categoryDTOs= categories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class))
				.toList();
		CategoryResponse categoryResponse= new CategoryResponse();
		categoryResponse.setContent(categoryDTOs);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setLastPage(categoryPage.isLast());
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
	public CategoryDTO deleteCategory(Long categoryId) {
		Category savedCategory = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));

		categoryRepository.delete(savedCategory);

		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
		// Update a category
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));

		existingCategory.setCategoryName(categoryDTO.getCategoryName());
		Category savedCategory = categoryRepository.save(existingCategory);
		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

}
