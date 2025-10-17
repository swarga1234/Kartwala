package com.swarga.Kartwala.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long addressId;

    @NotBlank
    @Size(max = 255, message = "Building name can't have more than 255 characters")
    private String buildingName;

    @NotBlank
    @Size(max = 255, message = "Street name can't have more than 255 characters")
    private String street;

    @NotBlank
    @Size(max = 255, message = "City name can't have more than 255 characters")
    private String city;

    @NotBlank
    @Size(max = 255, message = "Country name can't have more than 255 characters")
    private String country;

    @NotBlank
    @Size(max = 255, message = "State name can't have more than 255 characters")
    private String state;

    @NotBlank
    @Size(min = 6, max = 255, message = "pin code can't have more than 255 characters")
    private String pinCode;

}
