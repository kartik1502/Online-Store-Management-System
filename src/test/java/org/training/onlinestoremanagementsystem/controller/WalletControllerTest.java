package org.training.onlinestoremanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.service.WalletService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    @Test
    void testAddWallet() {

        String authToken = "lejkhtkljsrhtgkwjr.sqroiueiur.ethqrihtuioq35234thkrhktj";
        String walletType = "paytm";
        double balance = 700.00;

        ResponseDto expected = new ResponseDto("200","Wallet Added Successfully" );
        Mockito.when(walletService.addWallet(authToken, walletType, balance)).thenReturn(expected);
        ResponseEntity<ResponseDto> actual = walletController.addWallet(authToken, walletType, balance);
        assertNotNull(actual);
        assertEquals("Wallet Added Successfully", actual.getBody().getResponseMessage());
        assertEquals(201, actual.getStatusCodeValue());
    }
}
