package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.User;

import com.example.demo.repository.UserRepository;

public class MyUserDetailsService implements UserDetailsService
{
    @Autowired
	UserRepository userRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		
		System.out.println("user by email   (((((((((((((");
		
		
		User u = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found with email - "+username));
//		if(u == null)
//			throw new RuntimeException("Could not find user");
			
		System.out.println("user by email" + u);
		return MyUserDetails.build(u);
		
	}

}