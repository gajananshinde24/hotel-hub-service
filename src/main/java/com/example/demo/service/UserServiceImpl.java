package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.data.dto.UserCreationDTO;
import com.example.demo.data.dto.UserDTO;
import com.example.demo.data.dto.UserUpdateDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.User;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.model.response.ResponseBuilder;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public ResponseEntity<ApiResponse<UserDTO>> registerUser(UserCreationDTO userdto) {

		User user = mapper.map(userdto, User.class);
		
		user.setPassword(passwordEncoder.encode(userdto.getPassword()));

		User savedUser = userRepository.save(user);

		UserDTO userDTO2 = mapper.map(savedUser, UserDTO.class);

		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "User saved successfully", userDTO2);
	}

	@Override
	public ResponseEntity<ApiResponse<UserDTO>> getUser(UUID userid) {

		User user = userRepository.findById(userid).orElseThrow(() -> new ResourceNotFoundException("Canot find User with Id: " + userid)) ;

//
//		if (opUser.isEmpty()) {
//
//			throw new ResourceNotFoundException("Canot find User with Id: " + userid);
//
//		}
//		User user = opUser.get();
		
		UserDTO userDTO2 = mapper.map(user, UserDTO.class);
		return responseBuilder.buildResponse(HttpStatus.OK.value(), "User Data", userDTO2);

	}

	@Override
	public ResponseEntity<ApiResponse<UserDTO>> updateUser(UUID userId, UserUpdateDTO user) {

		
		User dbUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Canot find User with Id: " + userId)) ;

		if (user.getFirstName() != null) {
			dbUser.setFirstName(user.getFirstName());
		}
		if (user.getLastName() != null) {
			dbUser.setLastName(user.getLastName());
		}
		if (user.getNationality() != null) {
			dbUser.setNationality(user.getNationality());
		}
		if (user.getPassword() != null) {
			dbUser.setPassword(user.getPassword());
		}
		if (user.getPhoneNumber() != null) {
			dbUser.setPhoneNumber(user.getPhoneNumber());
		}
		if (user.getEmail() != null) {
			dbUser.setEmail(user.getEmail());
		}

		User savedUser = userRepository.save(dbUser);

		UserDTO userDTO2 = mapper.map(savedUser, UserDTO.class);

		return responseBuilder.buildResponse(HttpStatus.CREATED.value(), "User updated successfully", userDTO2);
	}

	@Override
	public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {

		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new ResourceNotFoundException("No users found in Database");
		}

		List<UserDTO> usersDTOList = new ArrayList<>();
		
		users.forEach((user) ->  usersDTOList.add(mapper.map(user, UserDTO.class)) );
		return responseBuilder.buildResponse(HttpStatus.OK.value(), "All users data", usersDTOList);
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Canot find User with email: " + email)) ;

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}

	@Override
	public User getUserByUserName(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Canot find User with email: " + email)) ;
		return user;
	}

}
