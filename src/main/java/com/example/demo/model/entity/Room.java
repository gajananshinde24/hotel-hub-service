package com.example.demo.model.entity;

import java.util.List;
import java.util.UUID;

import com.example.demo.enums.RoomType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
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
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID roomId;

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

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Room type cannot be null")
	private RoomType roomType;

	@ManyToOne
	@JoinColumn(name = "hotelId")
	private Hotel hotel;

	@ManyToMany
	@JoinTable( name = "booking_room", joinColumns = @JoinColumn(name = "roomId"), 
				inverseJoinColumns = @JoinColumn(name = "bookingId"))
	private List<Booking> bookings;
	

}
