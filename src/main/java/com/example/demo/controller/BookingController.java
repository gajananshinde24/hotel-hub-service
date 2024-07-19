package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.dto.BookingRequestDTO;
import com.example.demo.data.dto.BookingResponseDTO;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.BookingSevice;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
	
	@Autowired
	private BookingSevice bookingSevice;
	
	
	@PostMapping("")
	public ResponseEntity<ApiResponse<BookingResponseDTO>> addBooking(@RequestBody BookingRequestDTO bookingRequestDTO){
		return bookingSevice.addBooking(bookingRequestDTO);
	}

}
