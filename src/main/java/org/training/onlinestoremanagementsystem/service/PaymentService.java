package org.training.onlinestoremanagementsystem.service;

import org.training.onlinestoremanagementsystem.dto.ResponseDto;

public interface PaymentService {
    ResponseDto makePayment(String authToken, String walletId);
}
