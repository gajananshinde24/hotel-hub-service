package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.example.demo.data.dto.RoomRequestDTO;
import com.example.demo.data.dto.RoomResponseDTO;
import com.example.demo.data.dto.RoomUpdateDTO;
import com.example.demo.model.response.ApiResponse;

public interface RoomService {
	
	
	public ResponseEntity<ApiResponse<RoomResponseDTO>> addRoom(RoomRequestDTO roomResponseDTO);
	
	public ResponseEntity<ApiResponse<RoomResponseDTO>> getRoomById(UUID roomId);
	
	public ResponseEntity<ApiResponse<RoomResponseDTO>> updateRoom(UUID roomId, RoomUpdateDTO roomUpdateDTO);
	
	public ResponseEntity<ApiResponse<List<RoomResponseDTO>>> getAllRoomsByHotelId(UUID hotel);
	
	public ResponseEntity<ApiResponse<List<RoomResponseDTO>>> getAllRooms();
	
	
	
	

}
