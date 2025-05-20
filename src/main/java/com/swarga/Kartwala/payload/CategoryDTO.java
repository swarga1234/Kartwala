package com.swarga.Kartwala.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This is the pojo version of the Entity Category representing the request to the APIs by the client
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
	
	private Long categoryId;
	
	@NotBlank(message = "Category name can't be blank!!")
	@Size(min = 5, message = "Category name must contain atleast 5 characters!")
	private String categoryName;

}
