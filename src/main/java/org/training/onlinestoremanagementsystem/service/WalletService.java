package org.training.onlinestoremanagementsystem.service;

import org.training.onlinestoremanagementsystem.dto.ResponseDto;

public interface WalletService {
    ResponseDto addWallet(String authToken, String walletType, double walletBalance);
}
