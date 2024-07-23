package com.example.demo.data.dto;



import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.demo.enums.BookingStatus;

public class ExtendedBookingDTO {

    private UUID bookingId;
    private UUID userId;
    private UUID hotelId;
    private LocalDate bookingDate;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
    
    private HotelResponseDTO hotel;
    private List<RoomResponseDTO> rooms;

    // Constructors
    public ExtendedBookingDTO() {}

    public ExtendedBookingDTO(UUID bookingId, UUID userId, UUID hotelId, LocalDate bookingDate,
                              LocalDate checkInDate, LocalDate checkOutDate, BookingStatus status,
                              HotelResponseDTO hotel, List<RoomResponseDTO> rooms) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.hotelId = hotelId;
        this.bookingDate = bookingDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.hotel = hotel;
        this.rooms = rooms;
    }

    // Getters and Setters
    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getHotelId() {
        return hotelId;
    }

    public void setHotelId(UUID hotelId) {
        this.hotelId = hotelId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

	public HotelResponseDTO getHotel() {
		return hotel;
	}

	public void setHotel(HotelResponseDTO hotel) {
		this.hotel = hotel;
	}

	public List<RoomResponseDTO> getRooms() {
		return rooms;
	}

	public void setRooms(List<RoomResponseDTO> rooms) {
		this.rooms = rooms;
	}


}

