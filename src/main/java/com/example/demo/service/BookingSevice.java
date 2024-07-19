package com.example.demo.service;

import org.springframework.http.ResponseEntity;

import com.example.demo.data.dto.BookingRequestDTO;
import com.example.demo.data.dto.BookingResponseDTO;
import com.example.demo.model.response.ApiResponse;

public interface BookingSevice {
	
	public ResponseEntity<ApiResponse<BookingResponseDTO>> addBooking(BookingRequestDTO bookingRequestDTO);

}
