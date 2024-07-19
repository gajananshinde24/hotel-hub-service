package com.example.demo.data.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCreationDTO {
	
	 	@NotNull(message = "Email cannot be null")
	    @Email(message = "Email should be valid")
	    private String email;

	    @NotNull(message = "First name cannot be null")
	    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
	    private String firstName;

	    @Size(max = 30, message = "Last name cannot be longer than 30 characters")
	    private String lastName;

	    @NotNull(message = "Phone number cannot be null")
	    @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits")
	    private String phoneNumber;
	    
	    @NotNull(message = "Password cannot be null")
	    @Size(min = 8, message = "Password must be at least 8 characters")
	    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
	            message = "Password must have at least one uppercase letter, one lowercase letter, one digit, and one special character")
	    private String password;


	    @NotNull(message = "Nationality cannot be null")
	    @Size(min = 1, max = 50, message = "Nationality must be between 1 and 50 characters")
	    private String nationality;

}
