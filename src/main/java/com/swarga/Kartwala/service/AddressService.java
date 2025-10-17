package com.swarga.Kartwala.service;

import com.swarga.Kartwala.model.User;
import com.swarga.Kartwala.payload.AddressDTO;
import com.swarga.Kartwala.payload.AddressResponse;
import jakarta.validation.Valid;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    AddressResponse getAllAddresses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    AddressDTO getAddressById(Long addressId);

    AddressResponse getAllAddressesByUser(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, User user);

    AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO);

    AddressDTO deleteAddressById(Long addressId);
}
