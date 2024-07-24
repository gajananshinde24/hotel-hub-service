package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.enums.BookingStatus;
import com.example.demo.model.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
	
//	@Query("SELECT b FROM Booking b JOIN b.rooms r WHERE r.roomId = :roomId AND "
//		     + "(:checkIn <= b.checkOutDate AND :checkOut >= b.checkInDate)")
//		List<Booking> findOverlappingBookings(@Param("roomId") UUID roomId, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
	




    @Query("SELECT b FROM Booking b JOIN b.rooms r WHERE r.roomId = :roomId AND "
            + "(:checkIn <= b.checkOutDate AND :checkOut >= b.checkInDate) AND "
            + "b.status = :status")
     List<Booking> findOverlappingBookings(@Param("roomId") UUID roomId, 
                                           @Param("checkIn") LocalDate checkIn, 
                                           @Param("checkOut") LocalDate checkOut,
                                           @Param("status") BookingStatus status);
    
    Page<Booking> findByHotel_HotelId(UUID hotelId, Pageable pageable);
    
    List<Booking> findByUser_UserId(UUID userId);
    
    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);
	
	
}