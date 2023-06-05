package org.training.onlinestoremanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.onlinestoremanagementsystem.entity.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findCompanyByCompanyName(String companyName);

    List<Company> findCompanyByCompanyNameContainingIgnoreCase(String companyName);
}
