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
import org.training.onlinestoremanagementsystem.service.PaymentService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @Test
    void testMakePayment() {

        String authToken = "dkstjhrskjhtlkhqerwht.twrqthrkhtjkqrhthqritu.twqhwrt83485th4tkjlgrhjkh";
        String walletId = "1";

        ResponseDto responseDto = new ResponseDto("200", "Payment Successful");
        Mockito.when(paymentService.makePayment(authToken, walletId)).thenReturn(responseDto);

        ResponseEntity<ResponseDto> response = paymentController.makePayment(authToken, walletId);
        assertNotNull(response);
        assertEquals("Payment Successful", response.getBody().getResponseMessage());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}