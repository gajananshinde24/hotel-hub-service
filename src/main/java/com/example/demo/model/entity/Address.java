package com.example.demo.model.entity;



import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
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
@Embeddable
@ToString
public class Address {

	
	 	@NotBlank(message = "Street address cannot be blank")
	    @Size(max = 100, message = "Street address cannot be longer than 100 characters")
	    private String streetAddress;

	    @NotBlank(message = "City cannot be blank")
	    @Size(max = 50, message = "City cannot be longer than 50 characters")
	    private String city;

	    @NotBlank(message = "State cannot be blank")
	    @Size(max = 50, message = "State cannot be longer than 50 characters")
	    private String state;

	    @NotBlank(message = "Pin code cannot be blank")
	    @Pattern(regexp = "^\\d{6}$", message = "Pin code should be 6 digits")
	    private String pinCode;

	    @NotBlank(message = "Country cannot be blank")
	    @Size(max = 50, message = "Country cannot be longer than 50 characters")
	    private String country;
}
