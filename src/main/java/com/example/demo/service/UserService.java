package com.example.demo.service;


import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.data.dto.UserCreationDTO;
import com.example.demo.data.dto.UserDTO;
import com.example.demo.data.dto.UserUpdateDTO;
import com.example.demo.model.entity.User;
import com.example.demo.model.response.ApiResponse;
import java.util.List;

public interface UserService {
	
	public ResponseEntity<ApiResponse<UserDTO>> registerUser(UserCreationDTO user);
	
	public ResponseEntity<ApiResponse<UserDTO>> getUser(UUID userid);
	
	public ResponseEntity<ApiResponse<UserDTO>> updateUser(UUID userId, UserUpdateDTO user);
	
	
	public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers();
	
	public UserDetails loadUserByUsername(String email);
	
	public User getUserByUserName(String email);
	
	

}
