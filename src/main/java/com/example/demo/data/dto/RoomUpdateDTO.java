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
public class RoomUpdateDTO {

    
    @Min(value = 1, message = "Room number must be greater than 0")
    private Integer roomNumber;

  
    @Min(value = 1, message = "Number of persons must be greater than 0")
    private Integer noOfPerson;

    
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

 
    private Boolean isAvailable;

   
    private RoomType roomType;

    
    private UUID hotelId;
}
