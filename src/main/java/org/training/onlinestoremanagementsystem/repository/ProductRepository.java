package org.training.onlinestoremanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.onlinestoremanagementsystem.entity.Company;
import org.training.onlinestoremanagementsystem.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findProductByProductName(String productName);

    List<Product> findAllByProductNameContainingIgnoreCase(String productName);

    List<Product> findAllByCompanyIn(List<Company> companies);

    List<Product> findAllByProductNameContainingIgnoreCaseAndCompanyIn(String productName, List<Company> companies);

    List<Product> findAllByProductIdIn(List<Integer> productIds);
}
