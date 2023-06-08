package org.training.onlinestoremanagementsystem.service.implementation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.training.onlinestoremanagementsystem.dto.ProductDto;
import org.training.onlinestoremanagementsystem.dto.ResponseDto;
import org.training.onlinestoremanagementsystem.dto.UpdateProductDto;
import org.training.onlinestoremanagementsystem.dto.ViewProductDto;
import org.training.onlinestoremanagementsystem.entity.Company;
import org.training.onlinestoremanagementsystem.entity.Product;
import org.training.onlinestoremanagementsystem.exception.NoSuchProductExists;
import org.training.onlinestoremanagementsystem.repository.CompanyRepository;
import org.training.onlinestoremanagementsystem.repository.ProductRepository;
import org.training.onlinestoremanagementsystem.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Value("${spring.application.responseCode}")
    private String responseCode;

    @Override
    public ResponseDto addProduct(ProductDto productDto) {

        Product newProduct = new Product();
        Optional<Product> product = productRepository.findProductByProductName(productDto.getProductName());
        if (product.isPresent() && product.get().getCompany().getCompanyName().equals(productDto.getCompanyName())) {
            BeanUtils.copyProperties(product.get(), newProduct);
            newProduct.setQuantity(product.get().getQuantity() + productDto.getQuantity());
        }
        else {
            BeanUtils.copyProperties(productDto, newProduct, "companyName");
            Optional<Company> company = companyRepository.findCompanyByCompanyName(productDto.getCompanyName());
            if (company.isPresent()){
                newProduct.setCompany(company.get());
            }
            else{
                Company newCompany = new Company();
                newCompany.setCompanyName(productDto.getCompanyName());
                newProduct.setCompany(newCompany);
            }
        }
        productRepository.save(newProduct);
        return new ResponseDto(responseCode, "Product added successfully");
    }

    @Override
    public ResponseDto updateProduct(UpdateProductDto productDto, int productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NoSuchProductExists("Product with id " + productId + " does not exist"));

        BeanUtils.copyProperties(productDto, product);
        productRepository.save(product);
        return new ResponseDto(responseCode, "Product updated successfully");
    }

    @Override
    public List<ViewProductDto> getAllProducts(String productName, String companyName) {

        List<Product> products = null;
        if(Objects.isNull(productName) && !Objects.isNull(companyName)){
            List<Company> companies = companyRepository.findCompanyByCompanyNameContainingIgnoreCase(companyName);
            if (Boolean.FALSE.equals(companies.isEmpty())){
                products = productRepository.findAllByCompanyIn(companies);
            }
        }
        else if (Objects.isNull(companyName) && !Objects.isNull(productName)){
            products = productRepository.findAllByProductNameContainingIgnoreCase(productName);
        }
        else if (Objects.isNull(productName)) {
            products = productRepository.findAll();
        }
        else{
            List<Company> companies = companyRepository.findCompanyByCompanyNameContainingIgnoreCase(companyName);
            if (Boolean.FALSE.equals(companyName.isEmpty())){
                products = productRepository.findAllByProductNameContainingIgnoreCaseAndCompanyIn(productName, companies);
            }
        }
        List<ViewProductDto> requiredProducts = new ArrayList<>();
        if(!Objects.isNull(products)){
            products.forEach(product -> {
                ViewProductDto productDto = new ViewProductDto();
                BeanUtils.copyProperties(product, productDto);
                productDto.setCompanyName(product.getCompany().getCompanyName());
                requiredProducts.add(productDto);
            });
        }
        return requiredProducts;
    }
}
