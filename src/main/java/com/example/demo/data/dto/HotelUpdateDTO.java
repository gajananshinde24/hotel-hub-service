package com.example.demo.data.dto;

import java.util.UUID;

import com.example.demo.model.entity.Address;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelUpdateDTO {
	
   
    @Size(max = 100, message = "Hotel name cannot be longer than 100 characters")
    private String name;
  
    @Email(message = "Hotel email should be valid")
    private String hotelEmail;

    @Pattern(regexp = "^\\d{10}$", message = "Hotel phone should be 10 digits")
    private String hotelPhone;

    private Address address;
   
    private UUID ownerId;

}
