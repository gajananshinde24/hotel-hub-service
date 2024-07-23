package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Room;


@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
	
	List<Room> findByHotelHotelId(UUID hotelId);

}
