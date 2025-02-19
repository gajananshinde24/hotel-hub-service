package com.example.demo.service;

import com.example.demo.data.dto.UserCreationDTO;
import com.example.demo.data.dto.UserDTO;
import com.example.demo.enums.Role;
import com.example.demo.model.entity.User;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.model.response.ApiResponseBuilder;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApiResponseBuilder responseBuilder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void registerUser_EmailValidation() {
        // Arrange
        UserCreationDTO userDTO = new UserCreationDTO();
        userDTO.setEmail("gajanan@gmail.com");
        userDTO.setFirstName("Gajanan");
        userDTO.setLastName("Shinde");
        userDTO.setPhoneNumber("9028511428");
        userDTO.setNationality("INDIAN");
        userDTO.setRole(Role.ROLE_ADMIN);

        // Create a User object manually
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setNationality(userDTO.getNationality());
        user.setRole(userDTO.getRole());

        // Create UserDTO for response
        UserDTO userDTOResponse = new UserDTO();
        userDTOResponse.setEmail(user.getEmail());
        userDTOResponse.setFirstName(user.getFirstName());
        userDTOResponse.setLastName(user.getLastName());
        userDTOResponse.setPhoneNumber(user.getPhoneNumber());
        userDTOResponse.setNationality(user.getNationality());
        userDTOResponse.setRole(user.getRole());

        ApiResponse<UserDTO> apiResponse = new ApiResponse.ApiResponseBuilder<UserDTO>(201, "User saved successfully")
                .withData(userDTOResponse)
                .build()
                .getBody();


        ResponseEntity<ApiResponse<UserDTO>> expectedResponse =
                ResponseEntity.status(201).body(apiResponse);

        // Mock behavior
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTOResponse);
        when(responseBuilder.buildResponse(anyInt(), anyString(), any(UserDTO.class)))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userService.registerUser(userDTO);

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getBody(), "Response body should not be null");

        /*
        verify(userRepository, times(1)).save(any(User.class));
        verify(modelMapper, times(1)).map(userDTO, User.class);
        verify(modelMapper, times(1)).map(user, UserDTO.class);
        */
        // Assert response and email validation
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDTO.getEmail(), response.getBody().getData().getEmail());
    }
}