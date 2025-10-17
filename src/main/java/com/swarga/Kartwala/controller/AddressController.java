package com.swarga.Kartwala.controller;

import com.swarga.Kartwala.config.AppConstants;
import com.swarga.Kartwala.model.User;
import com.swarga.Kartwala.payload.AddressDTO;
import com.swarga.Kartwala.payload.AddressResponse;
import com.swarga.Kartwala.service.AddressService;
import com.swarga.Kartwala.utils.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtils authUtils;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtils.loggedInUser();
        AddressDTO createdAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<AddressDTO>(createdAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<AddressResponse> getAllAddresses(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                           @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                           @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_ADDRESS_BY, required = false) String sortBy,
                                                           @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortOrder){

        AddressResponse addressResponse = addressService.getAllAddresses(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<AddressResponse>(addressResponse, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<AddressResponse> getAddressByUser(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_ADDRESS_BY, required = false) String sortBy,
                                                            @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortOrder){
        User user= authUtils.loggedInUser();
        AddressResponse addressResponse = addressService.getAllAddressesByUser(pageNumber,pageSize,sortBy,sortOrder,user);
        return new ResponseEntity<AddressResponse>(addressResponse, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId,
                                                        @Valid @RequestBody AddressDTO addressDTO){
        AddressDTO updatedAddressDTO = addressService.updateAddressById(addressId, addressDTO);
        return new ResponseEntity<AddressDTO>(updatedAddressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddressById(@PathVariable Long addressId){
        User user = authUtils.loggedInUser();
        AddressDTO deletedAddressDTO= addressService.deleteAddressById(addressId);
        return new ResponseEntity<AddressDTO>(deletedAddressDTO, HttpStatus.OK);
    }
}
