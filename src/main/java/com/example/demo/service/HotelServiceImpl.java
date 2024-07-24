package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.data.dto.HotelRequestDTO;
import com.example.demo.data.dto.HotelResponseDTO;
import com.example.demo.data.dto.HotelUpdateDTO;
import com.example.demo.enums.Role;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Hotel;
import com.example.demo.model.entity.User;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.model.response.ResponseBuilder;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.UserRepository;

@Service
public class HotelServiceImpl implements HotelService {

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public ResponseEntity<ApiResponse<HotelResponseDTO>> registerHotel(HotelRequestDTO hotelRequestDTO) {
		User user = userRepository.findById(hotelRequestDTO.getOwnerId()).orElseThrow(
				() -> new ResourceNotFoundException("Canot find Owner with Id: " + hotelRequestDTO.getOwnerId()));

		if (user.getRole() != Role.HOTELOWNER) {
			throw new InvalidRequestException("The user is not a hotel owner");
		}

		Hotel hotel = mapper.map(hotelRequestDTO, Hotel.class);
		System.out.println("hotel id" + hotelRequestDTO.getOwnerId());

		hotel.setUser(user);

		Hotel savedHotel = hotelRepository.save(hotel);

		HotelResponseDTO hotelResponseDTO = mapper.map(savedHotel, HotelResponseDTO.class);

		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "Hotel registred succesfully",
				hotelResponseDTO);
	}

	@Override
	public ResponseEntity<ApiResponse<HotelResponseDTO>> updateHotel(UUID hotelId, HotelUpdateDTO hotelUpdateDTO) {

		Hotel hotel = hotelRepository.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Canot find hotel with Id" + hotelId));

		if (hotelUpdateDTO.getName() != null) {
			hotel.setName(hotelUpdateDTO.getName());
		}
		if (hotelUpdateDTO.getHotelEmail() != null) {
			hotel.setHotelEmail(hotelUpdateDTO.getHotelEmail());
		}
		if (hotelUpdateDTO.getHotelPhone() != null) {
			hotel.setHotelPhone(hotelUpdateDTO.getHotelPhone());
		}
		if (hotelUpdateDTO.getAddress() != null) {
			hotel.setAddress(hotelUpdateDTO.getAddress());
		}
		if (hotelUpdateDTO.getOwnerId() != null) {

			User user = userRepository.findById(hotelUpdateDTO.getOwnerId()).orElseThrow(
					() -> new ResourceNotFoundException("Canot find Owner with Id: " + hotelUpdateDTO.getOwnerId()));

			if (user.getRole() != Role.HOTELOWNER) {
				throw new InvalidRequestException("The user is not a hotel owner");
			}

			hotel.setName(hotelUpdateDTO.getName());
		}

		HotelResponseDTO hotelResponseDTO = mapper.map(hotel, HotelResponseDTO.class);

		return responseBuilder.buildResponse(HttpStatus.ACCEPTED.value(), "Hotel updated succesfully", hotelResponseDTO);
	}

	@Override
	public ResponseEntity<ApiResponse<HotelResponseDTO>> getHotelById(UUID hotelId) {

		Hotel hotel = hotelRepository.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Canot find hotel with Id" + hotelId));

		HotelResponseDTO hotelResponseDTO = mapper.map(hotel, HotelResponseDTO.class);
		return responseBuilder.buildResponse(HttpStatus.OK.value(), "Hotel data", hotelResponseDTO);
	}

	@Override
	public ResponseEntity<ApiResponse<HotelResponseDTO>> deleteHotel(UUID hotelId) {
		
		Hotel hotel = hotelRepository.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Canot find hotel with Id" + hotelId));
		hotelRepository.deleteById(hotelId);
		
		HotelResponseDTO hotelResponseDTO = mapper.map(hotel, HotelResponseDTO.class);
		return responseBuilder.buildResponse(HttpStatus.OK.value(), "Hotel deleted succesfully with Id -"+hotelId, hotelResponseDTO);
		
		
	}

	@Override
	public ResponseEntity<ApiResponse<List<HotelResponseDTO>>> getAllHotels(String searchBy, String filter, int page, int size, String sortBy) {
		
		
		 Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
	        Page<Hotel> hotelPage;
	        
	        System.out.println("Serach by ---"+searchBy +"Filter---"+filter);
         
	        switch (searchBy.toLowerCase()) {
	        case "name":
	            hotelPage = hotelRepository.findByNameContainingIgnoreCase(filter, pageable);
	            break;
	        case "city":
	            hotelPage = hotelRepository.findByAddressCityContainingIgnoreCase(filter, pageable);
	            break;
	        case "streetAddress":
	            hotelPage = hotelRepository.findByAddressStreetAddressContainingIgnoreCase(filter, pageable);
	            break;
	        case "state":
	            hotelPage = hotelRepository.findByAddressStateContainingIgnoreCase(filter, pageable);
	            break;
	        case "country":
	            hotelPage = hotelRepository.findByAddressCountryContainingIgnoreCase(filter, pageable);
	            break;
	        default:
	            hotelPage = hotelRepository.findAll(pageable);
	            break;
	    }
	        
	    Map<String, Object> map = new HashMap<>();
	    map.put("currentPage", page);
	    map.put("totalPages", hotelPage.getTotalPages());
	    map.put("pageSize", size);
	    map.put("currentPageCount", hotelPage.getNumberOfElements());
	    map.put("totalCount", hotelPage.getTotalElements());
	        
		List<HotelResponseDTO> hotelDTOList = new ArrayList<>();
		hotelPage.forEach((hotel) -> hotelDTOList.add(mapper.map(hotel, HotelResponseDTO.class)));
		return responseBuilder.buildResponse(HttpStatus.ACCEPTED.value(), "All hotels- ",hotelDTOList,map);
	}
	

}
