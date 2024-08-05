package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.example.demo.data.dto.BookingRequestDTO;
import com.example.demo.data.dto.BookingResponseDTO;
import com.example.demo.data.dto.ExtendedBookingDTO;
import com.example.demo.model.entity.Booking;
import com.example.demo.model.entity.Hotel;
import com.example.demo.model.response.ApiResponse;

public interface BookingSevice {
	
	public ResponseEntity<ApiResponse<BookingResponseDTO>> addBooking(BookingRequestDTO bookingRequestDTO);
	
	public ResponseEntity<ApiResponse<BookingResponseDTO>> updateBooking(UUID bookingId, BookingRequestDTO bookingRequestDTO);
	
	public ResponseEntity<ApiResponse<BookingResponseDTO>> cancelBooking(UUID bookingId);
	
	public ResponseEntity<ApiResponse<ExtendedBookingDTO>> getBookingById(UUID bookingId);
	
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByHotelId(UUID hotelId, int page, int size, String sortBy);
	
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByUserId(UUID userId);
	
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getAllBookings(String searchStatus, int page, int size, String sortBy);
	
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByHotelIdAndBookingDate(UUID hotelId);
	
	public String generateEmailBody(Hotel hotel, List<Booking> bookings);
	
	public void sendTodaysHotelBookingsEmail();
	
	
	
	
	

}
