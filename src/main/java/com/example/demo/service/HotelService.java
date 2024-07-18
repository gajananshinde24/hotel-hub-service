package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.example.demo.data.dto.HotelRequestDTO;
import com.example.demo.data.dto.HotelResponseDTO;
import com.example.demo.data.dto.HotelUpdateDTO;
import com.example.demo.model.response.ApiResponse;

public interface HotelService {
	
	
	public  ResponseEntity<ApiResponse<HotelResponseDTO>> registerHotel(HotelRequestDTO hotelRequestDTO);
	
	public  ResponseEntity<ApiResponse<HotelResponseDTO>> updateHotel(UUID hotelId, HotelUpdateDTO hotelUpdateDTO);
	
	
	public  ResponseEntity<ApiResponse<HotelResponseDTO>> getHotelById(UUID hotelId);
	
	public  ResponseEntity<ApiResponse<HotelResponseDTO>> deleteHotel(UUID hotelId);
	
	public ResponseEntity<ApiResponse<List<HotelResponseDTO>>> getAllHotels(String searchBy, String filter, int page, int size, String sortBy);
	
	
	

	

}
