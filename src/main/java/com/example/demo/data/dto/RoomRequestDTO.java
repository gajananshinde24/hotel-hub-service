package com.example.demo.data.dto;

import java.util.UUID;

import com.example.demo.enums.RoomType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequestDTO {

    @NotNull(message = "Room number cannot be null")
    @Min(value = 1, message = "Room number must be greater than 0")
    private Integer roomNumber;

    @NotNull(message = "Number of persons cannot be null")
    @Min(value = 1, message = "Number of persons must be greater than 0")
    private Integer noOfPerson;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    @NotNull(message = "Availability status cannot be null")
    private Boolean isAvailable;

    @NotNull(message = "Room type cannot be null")
    private RoomType roomType;

    @NotNull(message = "Hotel ID cannot be null")
    private UUID hotelId;
}
