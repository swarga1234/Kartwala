package com.swarga.Kartwala.repository;

import com.swarga.Kartwala.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT ad from Address ad JOIN ad.user u WHERE u.userId= ?1")
    Page<Address> findAddressByUserId(Long userId, Pageable pageDetails);
}
