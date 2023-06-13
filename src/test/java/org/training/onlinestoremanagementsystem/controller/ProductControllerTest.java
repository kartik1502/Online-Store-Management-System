package org.training.onlinestoremanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.dto.ProductDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.UpdateProductDto;
import org.training.onlinestoremanagementsystem.dto.ViewProductDto;
import org.training.onlinestoremanagementsystem.entity.Company;
import org.training.onlinestoremanagementsystem.entity.Product;
import org.training.onlinestoremanagementsystem.service.ProductService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    void testAddProduct() {

        ProductDto productDto = new ProductDto();
        productDto.setProductName("Talc");
        productDto.setCompanyName("Park Avenue");
        productDto.setPrice(90);
        productDto.setQuantity(10);

        ResponseDto responseDto = new ResponseDto("200", "Product added successfully");
        Mockito.when(productService.addProduct(productDto)).thenReturn(responseDto);

        ResponseEntity<ResponseDto> resultResponse = productController.addProduct(productDto);
        assertNotNull(resultResponse);
        assertEquals(HttpStatus.CREATED, resultResponse.getStatusCode());
        assertEquals("Product added successfully", resultResponse.getBody().getResponseMessage());
    }

    @Test
    void testUpdateProduct() {

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setPrice(100);
        updateProductDto.setQuantity(20);

        ResponseDto responseDto = new ResponseDto("200", "Product updated successfully");
        Mockito.when(productService.updateProduct(updateProductDto, 1)).thenReturn(responseDto);

        ResponseEntity<ResponseDto> resultResponse = productController.updateProduct(updateProductDto, 1);
        assertNotNull(resultResponse);
        assertEquals(HttpStatus.OK, resultResponse.getStatusCode());
        assertEquals("Product updated successfully", resultResponse.getBody().getResponseMessage());
    }

    @Test
    void testGetAllProducts() {

        List<ViewProductDto> products = new ArrayList<>();
        products.add(new ViewProductDto(1,"Talc",60 , "Ponds", 55));
        products.add(new ViewProductDto(2,"Talc",55 , "Park Avenue", 90));

        Mockito.when(productService.getAllProducts("T", "p")).thenReturn(products);
        ResponseEntity<List<ViewProductDto>> resultResponse = productController.getAllProducts("T", "p");
        assertNotNull(resultResponse);
        assertEquals(HttpStatus.OK, resultResponse.getStatusCode());
        assertEquals(2, resultResponse.getBody().size());
    }
}
