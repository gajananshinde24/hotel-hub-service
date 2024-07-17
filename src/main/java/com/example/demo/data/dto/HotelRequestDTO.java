package com.example.demo.data.dto;

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
public class HotelRequestDTO {

    @NotBlank(message = "Hotel name cannot be blank")
    @Size(max = 100, message = "Hotel name cannot be longer than 100 characters")
    private String name;

    @NotBlank(message = "Hotel email cannot be blank")
    @Email(message = "Hotel email should be valid")
    private String hotelEmail;

    @NotBlank(message = "Hotel phone cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Hotel phone should be 10 digits")
    private String hotelPhone;

    @NotNull(message = "Hotel address cannot be null")
    private Address address;
}