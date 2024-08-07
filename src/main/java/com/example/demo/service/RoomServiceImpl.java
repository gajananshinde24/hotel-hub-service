package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.data.dto.RoomRequestDTO;
import com.example.demo.data.dto.RoomResponseDTO;
import com.example.demo.data.dto.RoomUpdateDTO;
import com.example.demo.enums.BookingStatus;
import com.example.demo.exception.ResourceNotFoundException;

import com.example.demo.model.entity.Hotel;
import com.example.demo.model.entity.Room;

import com.example.demo.model.response.ApiResponse;

import com.example.demo.model.response.ApiResponseBuilder;

import com.example.demo.repository.BookingRepository;

import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomRepository;


@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ApiResponseBuilder responseBuilder;
	


	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Override
	public ResponseEntity<ApiResponse<RoomResponseDTO>> addRoom(RoomRequestDTO roomRequestDTO) {

		Hotel hotel = hotelRepository.findById(roomRequestDTO.getHotelId()).orElseThrow(
				() -> new ResourceNotFoundException("Canot find Hotel with Id" + roomRequestDTO.getHotelId()));

		Room room = mapper.map(roomRequestDTO, Room.class);
		room.setHotel(hotel);

		Room dbRoom = roomRepository.save(room);

		RoomResponseDTO roomResponseDTO = mapper.map(dbRoom, RoomResponseDTO.class);

		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "Room saved successfully", roomResponseDTO);
	}

	@Override
	public ResponseEntity<ApiResponse<RoomResponseDTO>> getRoomById(UUID roomId) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new ResourceNotFoundException("Canot find Room with Id" + roomId));
		RoomResponseDTO roomResponseDTO = mapper.map(room, RoomResponseDTO.class);
		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "Room Data", roomResponseDTO);
	}

	@Override
	public ResponseEntity<ApiResponse<RoomResponseDTO>> updateRoom(UUID roomId, RoomUpdateDTO roomUpdateDTO) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new ResourceNotFoundException("Canot find Room with Id" + roomId));

		if (roomUpdateDTO.getRoomNumber() != null) {
			room.setRoomNumber(roomUpdateDTO.getRoomNumber());
		}
		if (roomUpdateDTO.getNoOfPerson() != null) {
			room.setNoOfPerson(roomUpdateDTO.getNoOfPerson());
		}
		if (roomUpdateDTO.getPrice() != null) {
			room.setPrice(roomUpdateDTO.getPrice());
		}
		if (roomUpdateDTO.getRoomType() != null) {
			room.setRoomType(roomUpdateDTO.getRoomType());
		}
		if (roomUpdateDTO.getIsAvailable() != null) {
			room.setIsAvailable(roomUpdateDTO.getIsAvailable());
		}

		Room updatedRoom = roomRepository.save(room);

		RoomResponseDTO roomResponseDTO = mapper.map(updatedRoom, RoomResponseDTO.class);
		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "Room updated successfully", roomResponseDTO);
	}

	@Override
	public ResponseEntity<ApiResponse<List<RoomResponseDTO>>> getAllRoomsByHotelId(UUID hotelId) {

		Hotel hotel = hotelRepository.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Canot find Hotel with Id" + hotelId));

		List<Room> rooms = roomRepository.findByHotelHotelId(hotelId);
		if (rooms.isEmpty()) {
			throw new ResourceNotFoundException("No rooms are present in hotel with Id" + hotelId);
		}

		List<RoomResponseDTO> roomsDTOList = new ArrayList<>();
		rooms.forEach((room) -> roomsDTOList.add(mapper.map(room, RoomResponseDTO.class)));

		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "All rooms with HotelId - " + hotelId,
				roomsDTOList);
	}

	@Override
	public ResponseEntity<ApiResponse<List<RoomResponseDTO>>> getAllRooms() {

		List<Room> rooms = roomRepository.findAll();
		List<RoomResponseDTO> roomsDTOList = new ArrayList<>();
		rooms.forEach((room) -> roomsDTOList.add(mapper.map(room, RoomResponseDTO.class)));

		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "All room retrived successfully",
				roomsDTOList);
	}

	@Override
	public ResponseEntity<ApiResponse<List<RoomResponseDTO>>> getAllAvailableRooms() {

		List<Room> rooms = roomRepository.findAll();

		List<Room> bookedRooms = bookingRepository.findBookedRooms(BookingStatus.BOOKED);

		Set<UUID> bookedRoomIds = bookedRooms.stream().map(Room::getRoomId).collect(Collectors.toSet());

		List<Room> availableRooms = rooms.stream().filter(room -> !bookedRoomIds.contains(room.getRoomId()))
				.collect(Collectors.toList());

		if (availableRooms.isEmpty()) {
			throw new ResourceNotFoundException("Currently, there are no rooms available for booking.");
		}

		List<RoomResponseDTO> availableRoomDTOs = availableRooms.stream()
				.map(room -> mapper.map(room, RoomResponseDTO.class)).collect(Collectors.toList());

		return responseBuilder.buildResponse(HttpStatus.OK.value(), "Available rooms retrieved successfully",
				availableRoomDTOs);
	}

	@Override
	public ResponseEntity<ApiResponse<List<RoomResponseDTO>>> getAllAvailableRoomsByHotelId(UUID hotelId) {
		
		Hotel hotel = hotelRepository.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Canot find Hotel with Id" + hotelId));
		
		List<Room> hotelRooms = roomRepository.findByHotelHotelId(hotelId);
		
		List<Room> bookedRooms = bookingRepository.findBookedRoomsByHotelId(BookingStatus.BOOKED, hotelId);
		
		Set<UUID> bookedRoomIds = bookedRooms.stream().map(Room::getRoomId).collect(Collectors.toSet());

		List<Room> availableRooms = hotelRooms.stream().filter(room -> !bookedRoomIds.contains(room.getRoomId())).collect(Collectors.toList());
		
		if (availableRooms.isEmpty()) {
			throw new ResourceNotFoundException("Currently, there are no rooms available for booking in this Hotel");
		}

		List<RoomResponseDTO> availableRoomDTOs = availableRooms.stream()
				.map(room -> mapper.map(room, RoomResponseDTO.class)).collect(Collectors.toList());

		return responseBuilder.buildResponse(HttpStatus.OK.value(), "Available rooms retrieved successfully",
				availableRoomDTOs);
	
	}

}
