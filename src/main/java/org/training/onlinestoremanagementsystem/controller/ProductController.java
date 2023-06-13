package org.training.onlinestoremanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.training.onlinestoremanagementsystem.dto.ProductDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.UpdateProductDto;
import org.training.onlinestoremanagementsystem.dto.ViewProductDto;
import org.training.onlinestoremanagementsystem.service.ProductService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/internal/products")
    public ResponseEntity<ResponseDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.CREATED);
    }

    @PutMapping("/internal/products/{productId}")
    public ResponseEntity<ResponseDto> updateProduct(@Valid @RequestBody UpdateProductDto productDto, @PathVariable("productId") int productId) {
        return new ResponseEntity<>(productService.updateProduct(productDto, productId), HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ViewProductDto>> getAllProducts(@RequestParam(required = false) String productName, @RequestParam(required = false) String companyName) {
        return new ResponseEntity<>(productService.getAllProducts(productName, companyName), HttpStatus.OK);
    }

}
