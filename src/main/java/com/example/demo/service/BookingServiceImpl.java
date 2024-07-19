package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.data.dto.BookingRequestDTO;
import com.example.demo.data.dto.BookingResponseDTO;
import com.example.demo.enums.BookingStatus;
import com.example.demo.exception.BookingException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Booking;
import com.example.demo.model.entity.Hotel;
import com.example.demo.model.entity.Room;
import com.example.demo.model.entity.User;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.model.response.ResponseBuilder;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;

@Service
public class BookingServiceImpl implements BookingSevice {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	private ModelMapper mapper;

	@Override
	public ResponseEntity<ApiResponse<BookingResponseDTO>> addBooking(BookingRequestDTO bookingRequestDTO) {

		Hotel hotel = hotelRepository.findById(bookingRequestDTO.getHotelId()).orElseThrow(
				() -> new ResourceNotFoundException("Hotel not found with Id " + bookingRequestDTO.getHotelId()));

		User user = userRepository.findById(bookingRequestDTO.getUserId()).orElseThrow(
				() -> new ResourceNotFoundException("Canot find Owner with Id: " + bookingRequestDTO.getUserId()));

		List<Room> rooms = roomRepository.findAllById(bookingRequestDTO.getRoomIds());
		
		System.out.println("room id's    "+bookingRequestDTO.getRoomIds());

		if (rooms.size() != bookingRequestDTO.getRoomIds().size()) {
			throw new BookingException("One or more rooms not found for the given room IDs");
		}

		LocalDate checkIn = bookingRequestDTO.getCheckInDate();
		LocalDate checkOut = bookingRequestDTO.getCheckOutDate();

		for (Room room : rooms) {
			
			if (!room.getIsAvailable()) {
		            throw new BookingException("Room " + room.getRoomNumber() + " is not available.");
		        }
			 
			List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(room.getRoomId(), checkIn, checkOut);			
			System.out.println("overlappingBookings  " +overlappingBookings);

			if (!overlappingBookings.isEmpty()) {
				throw new BookingException("Room " + room.getRoomNumber() + " is not available for the selected dates");
			}
		}

		Booking booking = new Booking();
		booking.setHotel(hotel);
		booking.setRooms(rooms);
		booking.setUser(user);
		booking.setBookingDate(LocalDate.now());
		booking.setCheckInDate(checkIn);
		booking.setCheckOutDate(checkOut);
		booking.setStatus(BookingStatus.CONFIRMED);
		
		 for (Room room : rooms) {
		        room.setIsAvailable(false);
		        roomRepository.save(room); 
		 }
		 

		Booking savedBooking = bookingRepository.save(booking);

		BookingResponseDTO bookingResponseDTO = mapper.map(savedBooking, BookingResponseDTO.class);
		bookingResponseDTO.setRoomIds(bookingRequestDTO.getRoomIds());

		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "Booking successful", bookingResponseDTO);
	}

}
