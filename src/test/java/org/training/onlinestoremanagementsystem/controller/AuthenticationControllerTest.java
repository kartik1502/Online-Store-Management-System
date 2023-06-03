package org.training.onlinestoremanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.dto.AuthenticationRequest;
import org.training.onlinestoremanagementsystem.dto.AuthenticationResponse;
import org.training.onlinestoremanagementsystem.service.AuthenticationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    void testAuthenticateUser() {

        AuthenticationRequest request = new AuthenticationRequest("kartikkulkarni1411@gmail.com", "Ka3k@1411");

        Mockito.when(authenticationService.authenticate(request)).thenReturn(new AuthenticationResponse("200","sdfgsafjkhugre8t7e06we5r8ghf","Login Successfull"));

        ResponseEntity<AuthenticationResponse> testResponse = authenticationController.authenticateUser(request);
        assertNotNull(testResponse);
        assertEquals(HttpStatus.OK, testResponse.getStatusCode());
        assertEquals( "Login Successfull", testResponse.getBody().getResponseMessage());
    }
}
