package com.swarga.Kartwala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	@NotBlank(message = "Category name can't be blank!!")
	@Size(min = 5, message = "Category name must contain atleast 5 characters!")
	private String categoryName;

	@OneToMany(mappedBy = "category")
	private List<Product> products;
	
}
