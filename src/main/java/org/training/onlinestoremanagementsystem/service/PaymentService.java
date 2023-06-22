package org.training.onlinestoremanagementsystem.service;

import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.TransactionDto;

import java.util.List;

public interface PaymentService {
    ResponseDto makePayment(String authToken, String walletId);

    List<TransactionDto> getTransactions(String authToken);
}
