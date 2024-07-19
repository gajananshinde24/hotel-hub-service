package com.example.demo.data.dto;

import java.util.UUID;

import com.example.demo.enums.RoomType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {

    private UUID roomId;
    private Integer roomNumber;
    private Integer noOfPerson;
    private Double price;
    private Boolean isAvailable;
    private RoomType roomType;
    private UUID hotelId;
}
