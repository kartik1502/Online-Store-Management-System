package org.training.onlinestoremanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.onlinestoremanagementsystem.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
