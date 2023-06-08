package org.training.onlinestoremanagementsystem.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.training.onlinestoremanagementsystem.dto.ProductDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.UpdateProductDto;
import org.training.onlinestoremanagementsystem.dto.ViewProductDto;
import org.training.onlinestoremanagementsystem.entity.Company;
import org.training.onlinestoremanagementsystem.entity.Product;
import org.training.onlinestoremanagementsystem.exception.NoSuchProductExists;
import org.training.onlinestoremanagementsystem.repository.CompanyRepository;
import org.training.onlinestoremanagementsystem.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    void testAddProduct_CompanyAndProductNameRepeated() {

        Product product = new Product();
        product.setProductName("Talc");
        Company company = new Company();
        company.setCompanyName("Park Avenue");
        product.setCompany(company);
        product.setQuantity(10);
        product.setPrice(100.00);

        ProductDto productDto = new ProductDto();
        productDto.setProductName("Talc");
        productDto.setCompanyName("Park Avenue");
        productDto.setPrice(55.00);
        productDto.setQuantity(10);

        Mockito.when(productRepository.findProductByProductName(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);

        ResponseDto responseDto = productService.addProduct(productDto);

        assertNotNull(responseDto);
        assertEquals("Product added successfully", responseDto.getResponseMessage());

    }

    @Test
    void testAddProduct_CompanyRepeatedAndProductNameNotRepeated() {

        Mockito.when(productRepository.findProductByProductName(Mockito.anyString())).thenReturn(Optional.empty());

        ProductDto productDto = new ProductDto();
        productDto.setProductName("Talc");
        productDto.setCompanyName("Park Avenue");
        productDto.setPrice(55.00);
        productDto.setQuantity(10);

        Company company = new Company();
        company.setCompanyName("Park Avenue");

        Mockito.when(companyRepository.findCompanyByCompanyName(Mockito.anyString())).thenReturn(Optional.of(company));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(Mockito.any(Product.class));

        ResponseDto responseDto = productService.addProduct(productDto);

        assertNotNull(responseDto);
        assertEquals("Product added successfully", responseDto.getResponseMessage());
    }

    @Test
    void testAddProduct_CompanyNotRepeatedAndProductNameNotRepeated() {

        Mockito.when(productRepository.findProductByProductName(Mockito.anyString())).thenReturn(Optional.empty());

        ProductDto productDto = new ProductDto();
        productDto.setProductName("Talc");
        productDto.setCompanyName("Park Avenue");
        productDto.setPrice(55.00);
        productDto.setQuantity(10);

        Mockito.when(companyRepository.findCompanyByCompanyName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(Mockito.any(Product.class));

        ResponseDto responseDto = productService.addProduct(productDto);
        assertNotNull(responseDto);
        assertEquals("Product added successfully", responseDto.getResponseMessage());
    }

    @Test
    void testUpdateProduct_ProductNotFound() {

        Mockito.when(productRepository.findProductByProductName(Mockito.anyString())).thenReturn(Optional.empty());

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setPrice(55.00);
        updateProductDto.setQuantity(10);
        assertThrows(NoSuchProductExists.class, () -> productService.updateProduct(updateProductDto, 5));
    }

    @Test
    void testUpdateProduct_ProductFound() {

        Product product = new Product();
        product.setProductId(5);
        product.setProductName("Talc");
        product.setQuantity(10);
        product.setPrice(100.00);

        Company company = new Company();
        company.setCompanyName("Park Avenue");
        product.setCompany(company);

        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setPrice(55.00);
        updateProductDto.setQuantity(10);

        ResponseDto responseDto = productService.updateProduct(updateProductDto, 5);
        assertNotNull(responseDto);
        assertEquals("Product updated successfully", responseDto.getResponseMessage());
    }

    @Test
    void testGetAllProducts_ProductNameNotGivenAndCompanyNameGiven() {

        String companyName = "P";

        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        company.setCompanyName("Park Avenue");
        companies.add(company);
        Company company1 = new Company();
        company1.setCompanyName("Ponds");
        companies.add(company1);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        BeanUtils.copyProperties(new ProductDto("Talc",55 , "Park Avenue", 90), product);
        product.setCompany(company);
        products.add(product);
        Product product1 = new Product();
        BeanUtils.copyProperties(new ProductDto("Talc",60 , "Ponds", 55), product1);
        product1.setCompany(company1);
        products.add(product1);

        Mockito.when(companyRepository.findCompanyByCompanyNameContainingIgnoreCase(companyName)).thenReturn(companies);
        Mockito.when(productRepository.findAllByCompanyIn(companies)).thenReturn(products);

        List<ViewProductDto> resquiredProducts = productService.getAllProducts(null, companyName);
        assertNotNull(resquiredProducts);
        assertEquals(2, resquiredProducts.size());
    }

    @Test
    void testGetAllProducts_ProductNameNotGivenAndCompanyNameNotGiven() {

        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        company.setCompanyName("Park Avenue");
        companies.add(company);
        Company company1 = new Company();
        company1.setCompanyName("Ponds");
        companies.add(company1);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        BeanUtils.copyProperties(new ProductDto("Talc",55 , "Park Avenue", 90), product);
        product.setCompany(company);
        products.add(product);
        Product product1 = new Product();
        BeanUtils.copyProperties(new ProductDto("Talc",60 , "Ponds", 55), product1);
        product1.setCompany(company1);
        products.add(product1);

        Mockito.when(productRepository.findAll()).thenReturn(products);
        List<ViewProductDto> requiredProducts = productService.getAllProducts(null, null);
        assertNotNull(requiredProducts);
        assertEquals(2, requiredProducts.size());
    }

    @Test
    void testGetAllProducts_ProductNameGivenAndCompanyNameNotGiven() {

        String productName = "Talc";

        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        company.setCompanyName("Park Avenue");
        companies.add(company);
        Company company1 = new Company();
        company1.setCompanyName("Ponds");
        companies.add(company1);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        BeanUtils.copyProperties(new ProductDto("Talc",55 , "Park Avenue", 90), product);
        product.setCompany(company);
        products.add(product);
        Product product1 = new Product();
        BeanUtils.copyProperties(new ProductDto("Talc",60 , "Ponds", 55), product1);
        product1.setCompany(company1);
        products.add(product1);

        Mockito.when(productRepository.findAllByProductNameContainingIgnoreCase(productName)).thenReturn(products);
        List<ViewProductDto> requiredProducts = productService.getAllProducts(productName, null);
        assertNotNull(requiredProducts);
        assertEquals(2, requiredProducts.size());
    }

    @Test
    void testGetAllProducts_ProductNameGivenAndCompanyNameGiven() {

        String productName = "Talc";
        String companyName = "P";

        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        company.setCompanyName("Park Avenue");
        companies.add(company);
        Company company1 = new Company();
        company1.setCompanyName("Ponds");
        companies.add(company1);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        BeanUtils.copyProperties(new ProductDto("Talc",55 , "Park Avenue", 90), product);
        product.setCompany(company);
        products.add(product);
        Product product1 = new Product();
        BeanUtils.copyProperties(new ProductDto("Talc",60 , "Ponds", 55), product1);
        product1.setCompany(company1);
        products.add(product1);

        Mockito.when(companyRepository.findCompanyByCompanyNameContainingIgnoreCase(companyName)).thenReturn(companies);
        Mockito.when(productRepository.findAllByProductNameContainingIgnoreCaseAndCompanyIn(productName, companies)).thenReturn(products);

        List<ViewProductDto> requiredProducts = productService.getAllProducts(productName, companyName);
        assertNotNull(requiredProducts);
        assertEquals(2, requiredProducts.size());
    }

}
