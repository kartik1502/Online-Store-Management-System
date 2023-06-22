package org.training.onlinestoremanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.dto.CartQuantity;
import org.training.onlinestoremanagementsystem.dto.TransactionDto;
import org.training.onlinestoremanagementsystem.service.PaymentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private PaymentService paymentService;

    @Test
    void testGetTransactions() {

        String authToken = "skdfjghsfhgtsrhgta.rqkejrweigrqw4789.ewklfrhweqruqeitw";

        List<TransactionDto> transactionDtos = new ArrayList<>();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setFirstName("Karthik");
        transactionDto.setLastName("Kulkarni");
        transactionDto.setPaymentStatus("Cart Order Completed");
        transactionDto.setPaymentDate(LocalDate.now());
        transactionDto.setTotalPrice(300);

        List<CartQuantity> cartQuantities = new ArrayList<>();
        CartQuantity cartQuantity = new CartQuantity();
        cartQuantity.setProductId(1);
        cartQuantity.setQuantity(6);
        cartQuantity.setPrice(20);
        cartQuantity.setTotalPrice(120);
        cartQuantities.add(cartQuantity);
        cartQuantity = new CartQuantity();
        cartQuantity.setProductId(2);
        cartQuantity.setQuantity(9);
        cartQuantity.setPrice(20);
        cartQuantity.setTotalPrice(180);
        cartQuantities.add(cartQuantity);
        transactionDto.setCartQuantities(cartQuantities);
        transactionDtos.add(transactionDto);

        transactionDto = new TransactionDto();
        transactionDto.setFirstName("Karthik");
        transactionDto.setLastName("Kulkarni");
        transactionDto.setPaymentStatus("Cart Order Completed");
        transactionDto.setPaymentDate(LocalDate.now());
        transactionDto.setTotalPrice(300);

        cartQuantities = new ArrayList<>();
        cartQuantity = new CartQuantity();
        cartQuantity.setProductId(3);
        cartQuantity.setQuantity(6);
        cartQuantity.setPrice(56);
        cartQuantity.setTotalPrice(120);
        cartQuantities.add(cartQuantity);
        cartQuantity = new CartQuantity();
        cartQuantity.setProductId(2);
        cartQuantity.setQuantity(9);
        cartQuantity.setPrice(78);
        cartQuantity.setTotalPrice(180);
        cartQuantities.add(cartQuantity);
        transactionDto.setCartQuantities(cartQuantities);
        transactionDtos.add(transactionDto);

        Mockito.when(paymentService.getTransactions(authToken)).thenReturn(transactionDtos);

        ResponseEntity<List<TransactionDto>> listResponseEntity = transactionController.getTransactions(authToken);

        assertNotNull(listResponseEntity);
        assertEquals(2, listResponseEntity.getBody().size());
    }
}
