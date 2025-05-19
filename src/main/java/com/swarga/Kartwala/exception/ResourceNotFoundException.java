package com.swarga.Kartwala.exception;

import lombok.Getter;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	@Getter
	private String resourceName;
	@Getter
	private String field;
	@Getter
	private String fiedlName;
	@Getter
	private Long fieldId;
	
	public ResourceNotFoundException() {
		super();
		
	}

	public ResourceNotFoundException(String resourceName, String field, String fiedlName) {
		super(String.format("%s not found with %s: %s", resourceName, field, fiedlName));
		this.resourceName = resourceName;
		this.field = field;
		this.fiedlName = fiedlName;
	}

	public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
		super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
		this.resourceName = resourceName;
		this.field = field;
		this.fieldId = fieldId;
	}
	
	
	
	
}
