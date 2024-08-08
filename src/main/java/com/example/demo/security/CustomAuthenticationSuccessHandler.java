package com.example.demo.security;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.model.response.ResponseUtil;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ResponseUtil responseUtil;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		  OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		  	String email = oauth2User.getAttribute("email");

		    if (email == null || email.isEmpty()) {
		    	 clearSecurityContext(request, response);
		        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        response.setContentType("application/json");
		        String message = "Email not found in OAuth2 provider response";
		        String jsonResponse = responseUtil.generateJsonReponse("error", message, HttpServletResponse.SC_UNAUTHORIZED);
		        response.getWriter().write(jsonResponse);
		        return;
		    }

		    UserDetails userDetails;
		    try {
		        userDetails = userService.loadUserByUsername(email);
		        
		    } catch (Exception e) {
		    	 clearSecurityContext(request, response);
		        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        response.setContentType("application/json");
		        String message = "User not found in the database";
		        String jsonResponse = responseUtil.generateJsonReponse("error", message, HttpServletResponse.SC_UNAUTHORIZED);
		        response.getWriter().write(jsonResponse);
		        return;
		    }

		    String jwtToken = jwtTokenUtil.generateToken(userDetails);
		 
		  
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    String jsonResponse = responseUtil.generateJsonReponse("token", jwtToken, 200);
		    clearSecurityContext(request, response);
		    response.getWriter().write(jsonResponse);
		}
	
	private void clearSecurityContext(HttpServletRequest request, HttpServletResponse response) {
	    SecurityContextHolder.clearContext();
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate(); // Invalidate the session to clear any associated session data
	    }
	}
        

	

}
