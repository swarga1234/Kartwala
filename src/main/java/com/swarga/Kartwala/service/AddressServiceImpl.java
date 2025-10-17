package com.swarga.Kartwala.service;

import com.swarga.Kartwala.exception.APIException;
import com.swarga.Kartwala.exception.ResourceNotFoundException;
import com.swarga.Kartwala.model.Address;
import com.swarga.Kartwala.model.User;
import com.swarga.Kartwala.payload.AddressDTO;
import com.swarga.Kartwala.payload.AddressResponse;
import com.swarga.Kartwala.repository.AddressRepository;
import com.swarga.Kartwala.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        user.getAddresses().add(address);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public AddressResponse getAllAddresses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByandOrder=sortOrder.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Address> addressPage= addressRepository.findAll(pageDetails);

        List<Address> addresses= addressPage.getContent();
        if(addresses==null || addresses.isEmpty()){
            throw new APIException("No addresses available!!");
        }
        List<AddressDTO> addressDTOS =  addresses.stream().map( address -> modelMapper.map(address, AddressDTO.class)).toList();
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setContent(addressDTOS);
        addressResponse.setPageSize(addressPage.getSize());
        addressResponse.setPageNumber(addressPage.getNumber());
        addressResponse.setTotalElements(addressPage.getTotalElements());
        addressResponse.setTotalPages(addressPage.getTotalPages());
        addressResponse.setLastPage(addressPage.isLast());
        return addressResponse;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressResponse getAllAddressesByUser(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, User user) {
        Sort sortByandOrder=sortOrder.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageDetails= PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Address> addressPage= addressRepository.findAddressByUserId(user.getUserId(), pageDetails);
        List<Address> addresses= addressPage.getContent();
        if(addresses==null || addresses.isEmpty()){
            throw new APIException("No addresses available!!");
        }
        List<AddressDTO> addressDTOS =  addresses.stream().map( address -> modelMapper.map(address, AddressDTO.class)).toList();
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setContent(addressDTOS);
        addressResponse.setPageSize(addressPage.getSize());
        addressResponse.setPageNumber(addressPage.getNumber());
        addressResponse.setTotalElements(addressPage.getTotalElements());
        addressResponse.setTotalPages(addressPage.getTotalPages());
        addressResponse.setLastPage(addressPage.isLast());
        return addressResponse;
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {
       Address existingAddress= addressRepository.findById(addressId).orElseThrow(
               () -> new ResourceNotFoundException("Address","addressId", addressId));
       existingAddress.setBuildingName(addressDTO.getBuildingName());
       existingAddress.setCity(addressDTO.getCity());
       existingAddress.setCountry(addressDTO.getCountry());
       existingAddress.setState(addressDTO.getState());
       existingAddress.setStreet(addressDTO.getStreet());
       existingAddress.setPinCode(addressDTO.getPinCode());

       Address savedAddress= addressRepository.save(existingAddress);
       return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public AddressDTO deleteAddressById(Long addressId) {
        Address existingAddress= addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address","addressId", addressId));
        User user = existingAddress.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.deleteById(addressId);
        return modelMapper.map(existingAddress, AddressDTO.class);
    }
}
