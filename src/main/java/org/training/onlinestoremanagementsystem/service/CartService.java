package org.training.onlinestoremanagementsystem.service;

import org.training.onlinestoremanagementsystem.dto.AddToCartDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.ViewCartDto;

import java.util.List;

public interface CartService {
    ResponseDto addToCart(String authToken, List<AddToCartDto> addToCartDtos);

    ViewCartDto viewCart(String authToken);

    ResponseDto updateCart(String authToken, List<AddToCartDto> addToCartDtos);

    ResponseDto deleteCart(String authToken);
}
