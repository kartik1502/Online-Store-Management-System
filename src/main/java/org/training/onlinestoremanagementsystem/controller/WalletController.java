package org.training.onlinestoremanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.service.WalletService;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/wallets")
    public ResponseEntity<ResponseDto> addWallet(@RequestHeader(value = "Authorization", required = false) String authToken, String walletType, double balance) {
        return new ResponseEntity<>(walletService.addWallet(authToken, walletType, balance), HttpStatus.CREATED);
    }
}
