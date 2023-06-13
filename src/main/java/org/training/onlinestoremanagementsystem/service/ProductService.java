package org.training.onlinestoremanagementsystem.service;

import org.training.onlinestoremanagementsystem.dto.ProductDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.UpdateProductDto;
import org.training.onlinestoremanagementsystem.dto.ViewProductDto;

import java.util.List;

public interface ProductService {
    ResponseDto addProduct(ProductDto productDto);

    ResponseDto updateProduct(UpdateProductDto productDto, int productId);

    List<ViewProductDto> getAllProducts(String productName, String companyName);
}
