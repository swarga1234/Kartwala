package com.swarga.Kartwala.payload;

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
	private String categoryName;

}
