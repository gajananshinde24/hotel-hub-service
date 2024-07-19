package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.dto.RoomRequestDTO;
import com.example.demo.data.dto.RoomResponseDTO;
import com.example.demo.data.dto.RoomUpdateDTO;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.RoomService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

	
	@Autowired
	private RoomService roomService;
	
	
	@PostMapping("")
	public ResponseEntity<ApiResponse<RoomResponseDTO>> registerRoom(@RequestBody RoomRequestDTO roomRequestDTO){
		return roomService.addRoom(roomRequestDTO);
		
	}
	
	@GetMapping("")
	public ResponseEntity<ApiResponse<List<RoomResponseDTO>>> getAllRooms(){
		return roomService.getAllRooms();
	}
	
	@GetMapping("/hotel/{hotelId}")
	public ResponseEntity<ApiResponse<List<RoomResponseDTO>>> getAllRoomsByHotelId(@PathVariable UUID hotelId){
		return roomService.getAllRoomsByHotelId(hotelId);
	}
	
	@GetMapping("/{roomId}")
	public ResponseEntity<ApiResponse<RoomResponseDTO>> getRoomById(@PathVariable UUID roomId){
		return roomService.getRoomById(roomId);
	}
	
	@PutMapping("/{roomId}")
	public ResponseEntity<ApiResponse<RoomResponseDTO>> putMethodName(@PathVariable UUID roomId, @RequestBody RoomUpdateDTO roomUpdateDTO) {
		return roomService.updateRoom(roomId, roomUpdateDTO);
	}
	
	
	
	
	
}
