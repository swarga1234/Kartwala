package com.swarga.Kartwala.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Payload class to send generic API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {
	
	private String message;
	private boolean status;

}
