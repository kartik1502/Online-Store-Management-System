package org.training.onlinestoremanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.dto.AddToCartDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.ViewCartDto;
import org.training.onlinestoremanagementsystem.service.CartService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @Test
    void testAddToCart() {

        String authToken = "sflghfrt8934p5r4idsfkghfsdgdfjklghliorfoeiurio4678563427895hjdsfhjksdvjk";
        List<AddToCartDto> addToCartDtos = new ArrayList<>();
        addToCartDtos.add(new AddToCartDto(1,1));
        addToCartDtos.add(new AddToCartDto(2,2));

        Mockito.when(cartService.addToCart(authToken, addToCartDtos)).thenReturn(new ResponseDto("200", "Successfully added to cart"));

        ResponseEntity<ResponseDto> responseEntity = cartController.addToCart(authToken, addToCartDtos);
        assertNotNull(responseEntity);
    }

    @Test
    void testViewCart() {

        String authToken = "sflghfrt8934p5r4idsfkghfsdgdfjklghliorfoeiurio4678563427895hjdsfhjksdvjk";
        ViewCartDto viewCartDto = new ViewCartDto();
        viewCartDto.setFirstName("Karthik");
        viewCartDto.setLastName("S");
        viewCartDto.setContactNumber("6361921186");

        Mockito.when(cartService.viewCart(authToken)).thenReturn(viewCartDto);

        ResponseEntity<ViewCartDto> resultCart = cartController.viewCart(authToken);
        assertNotNull(resultCart);
    }

    @Test
    void testUpdateCart() {

        String authToken = "sflghfrt8934p5r4idsfkghfsdgdfjklghliorfoeiurio4678563427895hjdsfhjksdvjk";
        List<AddToCartDto> addToCartDtos = new ArrayList<>();
        addToCartDtos.add(new AddToCartDto(1,1));
        addToCartDtos.add(new AddToCartDto(2,2));

        Mockito.when(cartService.updateCart(authToken, addToCartDtos)).thenReturn(new ResponseDto("200", "Successfully updated cart"));

        ResponseEntity<ResponseDto> responseEntity = cartController.updateCart(authToken, addToCartDtos);
        assertNotNull(responseEntity);
    }

    @Test
    void testDeleteCart() {

        String authToken = "sflghfrt8934p5r4idsfkghfsdgdfjklghliorfoeiurio4678563427895hjdsfhjksdvjk";

        Mockito.when(cartService.deleteCart(authToken)).thenReturn(new ResponseDto("200", "Successfully deleted cart"));

        ResponseEntity<ResponseDto> responseEntity = cartController.deleteCart(authToken);
        assertNotNull(responseEntity);
    }
}

