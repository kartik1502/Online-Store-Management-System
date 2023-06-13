package org.training.onlinestoremanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.training.onlinestoremanagementsystem.dto.AddToCartDto;
import org.training.onlinestoremanagementsystem.dto.ProductQuantityDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.ViewCartDto;
import org.training.onlinestoremanagementsystem.service.CartService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<ResponseDto> addToCart(@RequestHeader(value = "Authorization", required = false) String authToken, @RequestBody List<AddToCartDto> addToCartDtos) {
        return new ResponseEntity<>(cartService.addToCart(authToken, addToCartDtos), HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<ViewCartDto> viewCart(@RequestHeader(value = "Authorization", required = false) String authToken) {
        return new ResponseEntity<>(cartService.viewCart(authToken), HttpStatus.OK);
    }

    @PutMapping("/carts")
    public ResponseEntity<ResponseDto> updateCart(@RequestHeader(value = "Authorization", required = false) String authToken, @RequestBody @Valid List<AddToCartDto> addToCartDtos) {
        return new ResponseEntity<>(cartService.updateCart(authToken, addToCartDtos), HttpStatus.OK);
    }

    @DeleteMapping("/cart")
    public ResponseEntity<ResponseDto> deleteCart(@RequestHeader(value = "Authorization", required = false) String authToken) {
        return new ResponseEntity<>(cartService.deleteCart(authToken), HttpStatus.OK);
    }
}
