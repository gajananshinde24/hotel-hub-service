package com.example.demo.data.dto;

import java.util.UUID;

import com.example.demo.model.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {

    private UUID hotelId;
    private String name;
    private String hotelEmail;
    private String hotelPhone;
    private Address address;
}
