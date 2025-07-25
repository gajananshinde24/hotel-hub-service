package com.example.demo.data.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.demo.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    private UUID bookingId;
    private UUID userId;
    private UUID hotelId;
    private List<RoomResponseDTO> rooms;
    private LocalDate bookingDate;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
}

