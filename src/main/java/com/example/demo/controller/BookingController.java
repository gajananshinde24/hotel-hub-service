package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.dto.BookingRequestDTO;
import com.example.demo.data.dto.BookingResponseDTO;
import com.example.demo.data.dto.ExtendedBookingDTO;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.BookingSevice;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

	@Autowired
	private BookingSevice bookingSevice;
	
	
	@PostMapping("")
	public ResponseEntity<ApiResponse<BookingResponseDTO>> addBooking(
			@RequestBody @Valid BookingRequestDTO bookingRequestDTO) {
		return bookingSevice.addBooking(bookingRequestDTO);
	}
	

	@PutMapping("/{bookingId}")
	public ResponseEntity<ApiResponse<BookingResponseDTO>> updateBooking(@PathVariable UUID bookingId,
			@RequestBody @Valid BookingRequestDTO bookingRequestDTO) {
		return bookingSevice.updateBooking(bookingId, bookingRequestDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("")
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getAllBookings(
			@RequestParam(defaultValue = "") String status, 	
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "bookingDate") String sortBy) {
		return bookingSevice.getAllBookings(status, page, size, sortBy);
	}
	
	
	@GetMapping("/{bookingId}")
	public ResponseEntity<ApiResponse<ExtendedBookingDTO>> getBookingById(@PathVariable UUID bookingId) {
		return bookingSevice.getBookingById(bookingId);
	}

	@PreAuthorize("hasAnyRole('ADMIN','HOTELOWNER')")
	@GetMapping("/hotel/{hotelId}")
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByHotelId(
			@PathVariable UUID hotelId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "bookingDate") String sortBy) {
		return bookingSevice.getBookingsByHotelId(hotelId, page, size, sortBy);
	}

	
	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByUserId(
			@PathVariable UUID userId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "bookingDate") String sortBy) {
		return bookingSevice.getBookingsByUserId(userId);
	}

	
	@PutMapping("/cancel/{bookingId}")
	public ResponseEntity<ApiResponse<BookingResponseDTO>> cancelBooking(@PathVariable UUID bookingId) {
		return bookingSevice.cancelBooking(bookingId);
	}

}
