package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.example.demo.model.response.ApiResponseBuilder;
import com.example.demo.service.HotelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {
	
	@Autowired
	public HotelService hotelService;
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ApiResponseBuilder responseBuilder;
	
	
	@PreAuthorize("hasAnyRole('ADMIN','HOTELOWNER')")
	@PostMapping("")
	public ResponseEntity<ApiResponse<HotelResponseDTO>> registerHotel(@RequestBody @Valid HotelRequestDTO hotelRequestDTO){
		return hotelService.registerHotel(hotelRequestDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','HOTELOWNER')")
	@PutMapping("/{hotelId}")
	public ResponseEntity<ApiResponse<HotelResponseDTO>> updateHotel(@PathVariable UUID hotelId, @RequestBody @Valid HotelUpdateDTO hotelUpdateDTO){
		HotelResponseDTO hotelResponseDTO = mapper.map(hotelService.updateHotel(hotelId, hotelUpdateDTO), HotelResponseDTO.class);

		return responseBuilder.buildResponse(HttpStatus.ACCEPTED.value(), "Hotel updated succesfully", hotelResponseDTO);
		 
		
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
	
	/**
	 * This method is currently commented out due to issues with deleting hotels.
	 *
	 *
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{hotelId}")
	public ResponseEntity<ApiResponse<HotelResponseDTO>> deleteHotelById(@PathVariable UUID hotelId){
		
		HotelResponseDTO hotelResponseDTO = mapper.map(hotelService.deleteHotel(hotelId), HotelResponseDTO.class);
		return responseBuilder.buildResponse(HttpStatus.OK.value(), "Hotel deleted succesfully with Id -"+hotelId, hotelResponseDTO);
		 
	}
	*/
	
	
	

}
