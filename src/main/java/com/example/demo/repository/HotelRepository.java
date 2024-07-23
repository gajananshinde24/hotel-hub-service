package com.example.demo.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
	
	Page<Hotel> findByNameContainingIgnoreCase(String filter, Pageable pageable);
	Page<Hotel> findByAddressCityContainingIgnoreCase(String filter, Pageable pageable);
	Page<Hotel> findByAddressStreetAddressContainingIgnoreCase(String filter, Pageable pageable);
	Page<Hotel> findByAddressStateContainingIgnoreCase(String filter, Pageable pageable);
	Page<Hotel> findByAddressCountryContainingIgnoreCase(String filter, Pageable pageable);
	
	
}