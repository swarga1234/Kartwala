package com.swarga.Kartwala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    /*
        address_id BIGINT
        building_name VARCHAR(255)
        city VARCHAR(255)
        country VARCHAR(255)
        pincode VARCHAR(255)
        state VARCHAR(255)
        street VARCHAR(255)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int addressId;
    @Column(name = "building_name")
    @NotBlank
    @Size(max = 255, message = "Building name can't have more than 255 characters")
    private String buildingName;

    @Column(name = "street")
    @NotBlank
    @Size(max = 255, message = "Street name can't have more than 255 characters")
    private String street;

    @Column(name = "city")
    @NotBlank
    @Size(max = 255, message = "City name can't have more than 255 characters")
    private String city;

    @NotBlank
    @Size(max = 255, message = "Country name can't have more than 255 characters")
    @Column(name = "country")
    private String country;

    @Column(name = "state")
    @NotBlank
    @Size(max = 255, message = "State name can't have more than 255 characters")
    private String state;

    @Column(name = "pincode")
    @NotBlank
    @Size(max = 255, message = "pin code can't have more than 255 characters")
    private String pinCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

}
