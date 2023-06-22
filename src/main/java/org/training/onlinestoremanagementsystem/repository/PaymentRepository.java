package org.training.onlinestoremanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.onlinestoremanagementsystem.entity.Cart;
import org.training.onlinestoremanagementsystem.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findAllByCartIn(List<Cart> carts);
}
