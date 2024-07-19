package com.example.demo.controller;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.dto.UserCreationDTO;
import com.example.demo.data.dto.UserDTO;
import com.example.demo.data.dto.UserUpdateDTO;
import com.example.demo.model.entity.User;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	public UserService userService;
	
	@GetMapping("/g")
	public ResponseEntity<String> greet() {
		 return ResponseEntity.ok("Hello from Spring Boot");
	}
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody  @Valid UserCreationDTO user) {
		
		System.out.println("user-->   "+user.getPassword());
		return userService.registerUser(user);
	}
	
	@GetMapping("")
	public  ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable UUID userId){
		return userService.getUser(userId);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable UUID userId, @RequestBody @Valid UserUpdateDTO user){
		return userService.updateUser(userId, user);
	}
	
	
	
	
	
	
	
	
	
	

}
