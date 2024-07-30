package com.example.demo.security;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		  OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		  	String email = oauth2User.getAttribute("email");

		    if (email == null || email.isEmpty()) {
		        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        response.setContentType("application/json");
		        String message = "Email not found in OAuth2 provider response";
		        response.getWriter().write("{\"error\": \"" + message + "\", \"statusCode\": " + HttpServletResponse.SC_UNAUTHORIZED + ", \"zonedDateTime\": \"" + ZonedDateTime.now() + "\"}");
		        return;
		    }

		    UserDetails userDetails;
		    try {
		        userDetails = userService.loadUserByUsername(email);
		    } catch (Exception e) {
		        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        response.setContentType("application/json");
		        String message = "User not found in the database";
		        response.getWriter().write("{\"error\": \"" + message + "\", \"statusCode\": " + HttpServletResponse.SC_UNAUTHORIZED + ", \"zonedDateTime\": \"" + ZonedDateTime.now() + "\"}");
		        return;
		    }

		    String jwtToken = jwtTokenUtil.generateToken(userDetails);
		 
		  
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write("{\"token\":\"" + jwtToken + "\", \"message\": \"Authentication successful\", \"statusCode\": 200, \"zonedDateTime\": \"" + ZonedDateTime.now() + "\"}");
		}
        

	

}
