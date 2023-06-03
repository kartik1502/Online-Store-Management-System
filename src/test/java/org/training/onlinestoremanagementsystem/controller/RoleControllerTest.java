package org.training.onlinestoremanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private UserService userService;

    @Test
    void testUpdateRole() {

        String authToken = "Bearer eyJhbGciNiJ9.eyJzdWIiOiJXMiOlsiZW1haWwiLCJyb2xlcyI6WyJ1coxNTg2NjAwNjIyfQ.qc6_Z-Gj9sjZcxYcxQ1W-7xqWxC9rkTvV_Xb2J1QfJ";
        String username = "kartikkulkarni1411@gmail.com";
        String role = "ADMIN";

        ResponseDto responseDto = new ResponseDto("200","Role updated successfully");

        Mockito.when(userService.updateRole(authToken, username, role)).thenReturn(responseDto);

        ResponseEntity<ResponseDto> testResponse = roleController.updateRole(authToken, username, role);
        assertNotNull(testResponse);
        assertEquals("200", testResponse.getBody().getResponseCode());
        assertEquals("Role updated successfully", testResponse.getBody().getResponseMessage());
        assertEquals("200 OK", testResponse.getStatusCode().toString());
    }
}

