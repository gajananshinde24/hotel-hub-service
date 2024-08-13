package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.data.dto.BookingRequestDTO;
import com.example.demo.data.dto.BookingResponseDTO;
import com.example.demo.data.dto.ExtendedBookingDTO;
import com.example.demo.enums.BookingStatus;
import com.example.demo.exception.BookingException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Booking;
import com.example.demo.model.entity.Hotel;
import com.example.demo.model.entity.Room;
import com.example.demo.model.entity.User;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.model.response.ApiResponseBuilder;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.EmailUtils;


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
	private ApiResponseBuilder responseBuilder;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private EmailUtils emailUtils;
	
	 @Autowired
	 private RabbitTemplate amqpTemplate;
	 
	 @Autowired
	  private Queue queue;

	@Override
    //@CacheEvict(value = "availableRooms", allEntries = true) 
	@Transactional
	public ResponseEntity<ApiResponse<BookingResponseDTO>> addBooking(BookingRequestDTO bookingRequestDTO) {

		Hotel hotel = hotelRepository.findById(bookingRequestDTO.getHotelId()).orElseThrow(
				() -> new ResourceNotFoundException("Hotel not found with Id " + bookingRequestDTO.getHotelId()));

		User user = userRepository.findById(bookingRequestDTO.getUserId()).orElseThrow(
				() -> new ResourceNotFoundException("Canot find User with Id: " + bookingRequestDTO.getUserId()));

		List<Room> rooms = roomRepository.findAllById(bookingRequestDTO.getRoomIds());

		System.out.println("room id's    " + bookingRequestDTO.getRoomIds());

		if (rooms.size() != bookingRequestDTO.getRoomIds().size()) {
			throw new BookingException("One or more rooms not found for the given room IDs");
		}

		LocalDate checkIn = bookingRequestDTO.getCheckInDate();
		LocalDate checkOut = bookingRequestDTO.getCheckOutDate();

		for (Room room : rooms) {

			List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(room.getRoomId(), checkIn,
					checkOut, BookingStatus.BOOKED);
			System.out.println("overlappingBookings  " + overlappingBookings);

			if (!overlappingBookings.isEmpty()) {
				throw new BookingException("Room " + room.getRoomNumber() + " is not available for the selected dates");
			}
		}

		Booking booking = new Booking();
		booking.setHotel(hotel);
		booking.setRooms(rooms);
		booking.setUser(user);
		booking.setBookingDate(bookingRequestDTO.getBookingDate());
		booking.setCheckInDate(checkIn);
		booking.setCheckOutDate(checkOut);
		booking.setStatus(BookingStatus.BOOKED);

		for (Room room : rooms) {
			room.setIsAvailable(false);
			//roomRepository.save(room);
		}

		Booking savedBooking = bookingRepository.save(booking);
		roomRepository.saveAll(rooms);
		

		BookingResponseDTO bookingResponseDTO = mapper.map(savedBooking, BookingResponseDTO.class);
		// bookingResponseDTO.setRoomIds(bookingRequestDTO.getRoomIds());
		
		amqpTemplate.convertAndSend("bookingExchange","routingkey", bookingResponseDTO);

		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "Booking successful", bookingResponseDTO);
	}

	@Override
	public ResponseEntity<ApiResponse<BookingResponseDTO>> updateBooking(UUID bookingId,
			BookingRequestDTO bookingRequestDTO) {

		Booking existingBooking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with Id " + bookingId));

		Hotel hotel = hotelRepository.findById(bookingRequestDTO.getHotelId()).orElseThrow(
				() -> new ResourceNotFoundException("Hotel not found with Id " + bookingRequestDTO.getHotelId()));
		existingBooking.setHotel(hotel);

		User user = userRepository.findById(bookingRequestDTO.getUserId()).orElseThrow(
				() -> new ResourceNotFoundException("Cannot find User with Id: " + bookingRequestDTO.getUserId()));
		existingBooking.setUser(user);

		List<Room> rooms = roomRepository.findAllById(bookingRequestDTO.getRoomIds());

		if (rooms.size() != bookingRequestDTO.getRoomIds().size()) {
			throw new BookingException("One or more rooms not found for the given room IDs");
		}

		LocalDate checkIn = bookingRequestDTO.getCheckInDate();
		LocalDate checkOut = bookingRequestDTO.getCheckOutDate();

		boolean isCheckOutDateExtended = checkOut.isAfter(existingBooking.getCheckOutDate());

		if (isCheckOutDateExtended) {
			for (Room room : rooms) {
				// Check for overlapping bookings only if the new check-out date extends beyond
				// the existing check-out date
				List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(room.getRoomId(), checkIn,
						checkOut, BookingStatus.BOOKED);

				// Exclude the current booking from the overlapping check
				if (overlappingBookings.stream()
						.anyMatch(b -> !b.getBookingId().equals(existingBooking.getBookingId()))) {
					throw new BookingException(
							"Room " + room.getRoomNumber() + " is not available for the selected dates");
				}
			}
		}
		existingBooking.setRooms(rooms);
		existingBooking.setBookingDate(LocalDate.now());
		existingBooking.setCheckInDate(checkIn);
		existingBooking.setCheckOutDate(checkOut);
		existingBooking.setStatus(BookingStatus.BOOKED);

		for (Room room : rooms) {
			room.setIsAvailable(false);
			roomRepository.save(room);
		}

		Booking updatedBooking = bookingRepository.save(existingBooking);

		BookingResponseDTO bookingResponseDTO = mapper.map(updatedBooking, BookingResponseDTO.class);
		// bookingResponseDTO.setRoomIds(bookingRequestDTO.getRoomIds());

		return responseBuilder.buildResponse(HttpStatus.OK.value(), "Booking updated successfully", bookingResponseDTO);

	}

	@Override
	public ResponseEntity<ApiResponse<ExtendedBookingDTO>> getBookingById(UUID bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Canot find Booking with Id -" + bookingId));

		ExtendedBookingDTO bookingDTO = mapper.map(booking, ExtendedBookingDTO.class);

		return responseBuilder.buildResponse(HttpStatus.OK.value(), "Booking Data", bookingDTO);
	}

	@Override
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByHotelId(UUID hotelId, int page, int size,
			String sortBy) {

		Hotel hotel = hotelRepository.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id " + hotelId));
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

		Page<Booking> hotelBookings = bookingRepository.findByHotel_HotelId(hotelId, pageable);

		List<BookingResponseDTO> hotelBookigsDTO = new ArrayList<>();
		hotelBookings.forEach((booking) -> hotelBookigsDTO.add(mapper.map(booking, BookingResponseDTO.class)));

		Map<String, Object> map = new HashMap<>();
		map.put("currentPage", page);
		map.put("totalPages", hotelBookings.getTotalPages());
		map.put("pageSize", size);
		map.put("currentPageCount", hotelBookings.getNumberOfElements());
		map.put("totalCount", hotelBookings.getTotalElements());

		return responseBuilder.buildResponse(HttpStatus.OK.value(),
				"All bookings associated with hotel Id :- " + hotelId, hotelBookigsDTO, map);
	}

	@Override
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByUserId(UUID userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cannot find User with Id: " + userId));

		List<Booking> userBookings = bookingRepository.findByUser_UserId(userId);
		List<BookingResponseDTO> hotelBookigsDTO = new ArrayList<>();

		userBookings.forEach((booking) -> hotelBookigsDTO.add(mapper.map(booking, BookingResponseDTO.class)));
		Map<String, Object> map = new HashMap<>();

		map.put("Total No of bookings: ", userBookings.size());

		return responseBuilder.buildResponse(HttpStatus.OK.value(), "All bookings associated with User Id :- " + userId,
				hotelBookigsDTO, map);
	}

	@Override
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getAllBookings(String searchStatus, int page, int size,
			String sortBy) {
		List<BookingResponseDTO> bookingsDTO = new ArrayList<>();
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

		Page<Booking> bookingPage;

		if (searchStatus == null || searchStatus.trim().isEmpty()) {
			bookingPage = bookingRepository.findAll(pageable);
		} else {
			BookingStatus status = BookingStatus.valueOf(searchStatus.toUpperCase());
			bookingPage = bookingRepository.findByStatus(status, pageable);
		}

		bookingPage.forEach(booking -> bookingsDTO.add(mapper.map(booking, BookingResponseDTO.class)));

		Map<String, Object> map = new HashMap<>();
		map.put("currentPage", page);
		map.put("totalPages", bookingPage.getTotalPages());
		map.put("pageSize", size);
		map.put("currentPageCount", bookingPage.getNumberOfElements());
		map.put("totalCount", bookingPage.getTotalElements());

		return responseBuilder.buildResponse(HttpStatus.OK.value(), "All bookings", bookingsDTO, map);

	}

	@Override
	public ResponseEntity<ApiResponse<BookingResponseDTO>> cancelBooking(UUID bookingId) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Canot find Booking with Id -" + bookingId));

		if (booking.getCheckInDate().isBefore(LocalDate.now()) || booking.getCheckInDate().equals(LocalDate.now())) {
			throw new BookingException("Booking cannot be cancelled now!!!");
		}

		booking.setStatus(BookingStatus.CANCELLED);

		for (Room room : booking.getRooms()) {
			room.setIsAvailable(true);
			roomRepository.save(room);
		}

		Booking updatedBooking = bookingRepository.save(booking);

		BookingResponseDTO bookingResponseDTO = mapper.map(updatedBooking, BookingResponseDTO.class);

		return responseBuilder.buildResponse(HttpStatus.OK.value(), "Booking cancelled successfully",
				bookingResponseDTO);
	}

	@Override
	public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByHotelIdAndBookingDate(UUID hotelId) {
		
		Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id" + hotelId));
		
		LocalDate today = LocalDate.now();
		
		List<Booking> bookings = bookingRepository.findByHotelIdAndBookingDate(hotelId, today);
		
		
		  List<BookingResponseDTO> bookingDTOs = bookings.stream()
		            .map(booking -> mapper.map(booking, BookingResponseDTO.class))
		            .collect(Collectors.toList());

		 return  responseBuilder.buildResponse(HttpStatus.OK.value(), "Bookings retrieved successfully", bookingDTOs);
		
		
	}

	@Override
	public String generateEmailBody(Hotel hotel, List<Booking> bookings) {
		   StringBuilder emailBody = new StringBuilder();
		    emailBody.append("<html><body>");
		    emailBody.append("<p>Dear ").append(hotel.getName()).append(" Team,</p>");
		    emailBody.append("<p>Here are the bookings for today (").append(LocalDate.now()).append("):</p>");

		    for (Booking booking : bookings) {
		        emailBody.append("<p><strong>Booking ID:</strong> ").append(booking.getBookingId()).append("<br>")
		                 .append("<strong>User:</strong> ").append(booking.getUser().getFirstName()).append("<br>")
		                 .append("<strong>Rooms:</strong> ").append(booking.getRooms().stream().map(room -> "Room No - "+String.valueOf(room.getRoomNumber()))	
		                 .collect(Collectors.joining(","))).append("<br>")
		                 .append("<strong>Check-in Date:</strong> ").append(booking.getCheckInDate()).append("<br>")
		                 .append("<strong>Check-out Date:</strong> ").append(booking.getCheckOutDate()).append("</p>");
		    }

		    emailBody.append("<p>Best regards,<br>")
		             .append("Your Hotel Management System</p>");
		    emailBody.append("</body></html>");

	        return emailBody.toString();
	}
	
	
	 public void sendTodaysHotelBookingsEmail() {
	        List<Hotel> hotels = hotelRepository.findAll();
	        LocalDate today = LocalDate.now();

	        for (Hotel hotel : hotels) {
	            List<Booking> bookings = bookingRepository.findByHotelIdAndBookingDate(hotel.getHotelId(), today);

	            if (!bookings.isEmpty()) {
	            
	                String emailBody = generateEmailBody(hotel, bookings);

	              
	                User owner = userRepository.findById(hotel.getUser().getUserId())
	                        .orElseThrow(() -> new RuntimeException("Owner not found for hotel " + hotel.getHotelId()));
	              emailUtils.sendEmail(owner.getEmail(), "Today's Bookings for Hotel: " + hotel.getName(), emailBody);
	            }
	        }
	    }
	 	
	    //@Scheduled(cron = "* * * * * ?") 
		@Scheduled(cron = "* * 23 * * ?") 
		public void scheduleEmailSending() {
			sendTodaysHotelBookingsEmail();
		}
	 

}
