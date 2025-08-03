package com.swarga.Kartwala.exception;

import lombok.Getter;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	@Getter
	private String resourceName;
	@Getter
	private String field;
	@Getter
	private String fieldName;
	@Getter
	private Long fieldId;
	
	public ResourceNotFoundException() {
		super();
		
	}

	public ResourceNotFoundException(String resourceName, String field, String fieldName) {
		super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldName = fieldName;
	}

	public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
		super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}
	
	
}
