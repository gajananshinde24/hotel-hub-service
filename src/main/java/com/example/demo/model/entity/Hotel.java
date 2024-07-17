package com.example.demo.model.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hotelId;

    @NotBlank(message = "Hotel name cannot be blank")
    @Size(max = 100, message = "Hotel name cannot be longer than 100 characters")
    private String name;

    @NotBlank(message = "Hotel email cannot be blank")
    @Email(message = "Hotel email should be valid")
    @Column(unique = true)
    private String hotelEmail;

    @NotBlank(message = "Hotel phone cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Hotel phone should be 10 digits")
    @Column(unique = true)
    private String hotelPhone;

    @Embedded
    @NotNull(message = "Hotel address cannot be null")
    private Address address;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}

