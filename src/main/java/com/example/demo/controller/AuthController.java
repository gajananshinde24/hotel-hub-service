package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.dto.LoginRequest;
import com.example.demo.data.dto.UserDTO;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.model.entity.User;
import com.example.demo.model.response.ApiResponseBuilder;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private ApiResponseBuilder responseBuilder;

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper mapper;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {

		Authentication authentication = null;

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		} catch (Exception e) {

			throw new InvalidRequestException("Invalid email or password");

		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		


		 String jwt = jwtTokenUtil.generateToken(userDetails);

		String email = jwtTokenUtil.getUsernameFromToken(jwt);
		User user = userService.getUserByUserName(email);

		UserDTO userDTO = mapper.map(user, UserDTO.class);

		Map<String, Object> map = new HashMap<>();
		map.put("jwtToken", jwt);

		return responseBuilder.buildResponse(HttpStatus.ACCEPTED.value(), "Login Succesfull", userDTO, map);
	}

}
