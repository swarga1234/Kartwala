package com.swarga.Kartwala.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
 * This the response of the API for the Category
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
	
	private List<CategoryDTO> content;

}
