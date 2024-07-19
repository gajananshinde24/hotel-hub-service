package com.example.demo.filter;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	@Lazy
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws ServletException, IOException {

	    final String authorizationHeader = request.getHeader("Authorization");

	    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
	        handleException(response, "Token is not present in header", HttpStatus.UNAUTHORIZED);
	        return;
	    }

	    String jwt = authorizationHeader.substring(7);
	    String username = jwtTokenUtil.getUsernameFromToken(jwt);

	    if (username == null) {
	        handleException(response, "Invalid token", HttpStatus.UNAUTHORIZED);
	        return;
	    }

	    if (SecurityContextHolder.getContext().getAuthentication() == null) {
	        UserDetails userDetails = this.userService.loadUserByUsername(username);

	        if (!jwtTokenUtil.validateToken(jwt, userDetails)) {
	            handleException(response, "Invalid token", HttpStatus.UNAUTHORIZED);
	            return;
	        }

	        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
	                userDetails, null, userDetails.getAuthorities());
	        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	    }

	    chain.doFilter(request, response);
	}
	
	private void handleException(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"errorMessage\": \"" + message + "\", \"statusCode\": " + status.value() + ", \"zonedDateTime\": \"" + ZonedDateTime.now() + "\"}");
    }
	
	 
}
