package com.example.demo.data.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotNull(message = "User ID cannot be null")
    private UUID userId;

    @NotNull(message = "Hotel ID cannot be null")
    private UUID hotelId;

    @NotNull(message = "Room IDs cannot be null")
    private List<UUID> roomIds;

    @NotNull(message = "Booking date cannot be null")
    @FutureOrPresent(message = "Booking date cannot be in the past")
    private LocalDate bookingDate;

    @NotNull(message = "Check-in date cannot be null")
    @FutureOrPresent(message = "Check-in date cannot be in the past")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date cannot be null")
    @FutureOrPresent(message = "Check-out date cannot be in the past")
    private LocalDate checkOutDate;
}
