package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.dto.HotelRequestDTO;
import com.example.demo.data.dto.HotelResponseDTO;
import com.example.demo.data.dto.HotelUpdateDTO;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.HotelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {
	
	@Autowired
	public HotelService hotelService;
	
	
	@PostMapping("")
	public ResponseEntity<ApiResponse<HotelResponseDTO>> registerHotel(@RequestBody @Valid HotelRequestDTO hotelRequestDTO){
		return hotelService.registerHotel(hotelRequestDTO);
	}
	
	@PutMapping("/{hotelId}")
	public ResponseEntity<ApiResponse<HotelResponseDTO>> updateHotel(@PathVariable UUID hotelId, @RequestBody @Valid HotelUpdateDTO hotelUpdateDTO){
		return hotelService.updateHotel(hotelId, hotelUpdateDTO);
		
	}
	
	@GetMapping("")
	public ResponseEntity<ApiResponse<List<HotelResponseDTO>>> getAllHotels(
		    @RequestParam(defaultValue = "") String searchBy,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy){
		return hotelService.getAllHotels(searchBy, filter, page, size, sortBy);
	}
	
	@GetMapping("/{hotelId}")
	public ResponseEntity<ApiResponse<HotelResponseDTO>> getHotelbyId(@PathVariable UUID hotelId){
		return hotelService.getHotelById(hotelId);
	}
	
	@DeleteMapping("/{hotelId}")
	public ResponseEntity<ApiResponse<HotelResponseDTO>> deleteHotelById(@PathVariable UUID hotelId){
		return hotelService.deleteHotel(hotelId);
	}
	
	
	

}
