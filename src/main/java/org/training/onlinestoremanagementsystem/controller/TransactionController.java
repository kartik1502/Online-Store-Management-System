package org.training.onlinestoremanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.training.onlinestoremanagementsystem.dto.TransactionDto;
import org.training.onlinestoremanagementsystem.service.PaymentService;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payments")
    public ResponseEntity<List<TransactionDto>> getTransactions(@RequestHeader(value = "Authorization", required = false) String authToken) {
        return new ResponseEntity<>(paymentService.getTransactions(authToken), HttpStatus.OK);
    }
}
