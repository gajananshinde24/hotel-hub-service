package com.example.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.dto.BookingResponseDTO;
import com.example.demo.data.dto.RoomResponseDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Hotel;
import com.example.demo.model.entity.Room;
import com.example.demo.model.entity.User;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.EmailUtils;

@Service
public class EmailService {
	
		@Autowired
		private EmailUtils emailUtils;
		
		@Autowired
		private HotelRepository hotelRepository;
		
		@Autowired
		private UserRepository userRepository;
	
	 	@RabbitListener(queues = "bookingQueue")
	    public void receiveMessage(BookingResponseDTO bookingDetails) {
	 		
	 		
	 		Hotel hotel = hotelRepository.findById(bookingDetails.getHotelId())
					.orElseThrow(() -> new ResourceNotFoundException("Canot find hotel with Id" + bookingDetails.getHotelId()));
	        String mailBody = generateBookingEmailBody(bookingDetails, hotel);
	        
	    	User user = userRepository.findById(bookingDetails.getUserId()).orElseThrow(
					() -> new ResourceNotFoundException("Canot find Owner with Id: " + bookingDetails.getUserId()));

	        
	        emailUtils.sendEmail(user.getEmail(), "Your Booking Deatils", mailBody);
	    }
	 	
	    public String generateBookingEmailBody(BookingResponseDTO bookingResponseDTO, Hotel hotel) {
	        StringBuilder htmlBuilder = new StringBuilder();

	        htmlBuilder.append("<html>")
	                   .append("<body>")
	                   .append("<h1>Booking Confirmation</h1>")
	                   .append("<p><strong>Hotel Name:</strong> ").append(hotel.getName()).append("</p>")
	                   .append("<p><strong>Hotel Address:</strong> ")
	                   .append(hotel.getAddress().getStreetAddress()).append(", ")
	                   .append(hotel.getAddress().getCity()).append(", ")
	                   .append(hotel.getAddress().getState()).append(", ")
	                   .append(hotel.getAddress().getPinCode())
	                   .append("</p>")
	                   .append("<p><strong>Booking ID:</strong> ").append(bookingResponseDTO.getBookingId()).append("</p>")
	                   .append("<p><strong>User ID:</strong> ").append(bookingResponseDTO.getUserId()).append("</p>")
	                   .append("<p><strong>Hotel ID:</strong> ").append(bookingResponseDTO.getHotelId()).append("</p>")
	                   .append("<p><strong>Booking Date:</strong> ").append(bookingResponseDTO.getBookingDate()).append("</p>")
	                   .append("<p><strong>Check-In Date:</strong> ").append(bookingResponseDTO.getCheckInDate()).append("</p>")
	                   .append("<p><strong>Check-Out Date:</strong> ").append(bookingResponseDTO.getCheckOutDate()).append("</p>")
	                   .append("<p><strong>Status:</strong> ").append(bookingResponseDTO.getStatus()).append("</p>")
	                   .append("<h2>Rooms:</h2>")
	                   .append("<ul>");

	        for (RoomResponseDTO room : bookingResponseDTO.getRooms()) {
	            htmlBuilder.append("<li>")
	                       .append("Room ID: ").append(room.getRoomId()).append(", ")
	                       .append("Room Number: ").append(room.getRoomNumber())
	                       .append("</li>");
	        }

	        htmlBuilder.append("</ul>")
	                   .append("</body>")
	                   .append("</html>");

	        return htmlBuilder.toString();
	    }

}
