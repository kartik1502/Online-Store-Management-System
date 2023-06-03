package org.training.onlinestoremanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.UserDto;
import org.training.onlinestoremanagementsystem.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private UserService userService;

    @Test
    void testRegisterUser() {

        UserDto userDto = new UserDto();
        userDto.setEmailId("kartikkulkarni1411@gmail.com");
        userDto.setPassword("Ka3k@1411");
        userDto.setRepeatPassword("Ka3k@1411");

        ResponseDto responseDto = new ResponseDto("200", "User registered successfully");
        Mockito.when(userService.registerUser(userDto)).thenReturn(responseDto);

        ResponseEntity<ResponseDto> testResponse = registrationController.registerUser(userDto);
        assertNotNull(testResponse);
        assertEquals(HttpStatus.CREATED, testResponse.getStatusCode());
        assertEquals("User registered successfully", testResponse.getBody().getResponseMessage());
    }
}
