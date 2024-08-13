package com.example.demo.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.demo.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {



	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookingId;
    
    //@JsonBackReference(value = "user-booking")
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotelId")
   // @JsonBackReference(value = "hotel-booking")
    private Hotel hotel;

//    @ManyToMany(mappedBy = "bookings")
//    private List<Room> rooms;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "booking_room", 
               joinColumns = @JoinColumn(name = "bookingId"), 
               inverseJoinColumns = @JoinColumn(name = "roomId"))
//    @JsonManagedReference(value = "booking-room-ref")
   // @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "roomId")
    private List<Room> rooms;

    @NotNull(message = "Booking date cannot be null")
    @FutureOrPresent(message = "Booking date cannot be in the past")
    private LocalDate bookingDate;

    @NotNull(message = "Check-in date cannot be null")
    @FutureOrPresent(message = "Check-in date cannot be in the past")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date cannot be null")
    @FutureOrPresent(message = "Check-out date cannot be in the past")
    private LocalDate checkOutDate;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Booking status cannot be null")
    private BookingStatus status;
}
