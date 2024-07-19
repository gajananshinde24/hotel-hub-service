package com.example.demo.data.dto;



import java.util.UUID;

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
public class UserDTO {

	private UUID userId;

    
    private String email;

  
    private String firstName;

    
    private String lastName;

   
    private String phoneNumber;
    
    private String nationality;

    

}