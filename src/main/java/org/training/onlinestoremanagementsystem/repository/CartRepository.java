package org.training.onlinestoremanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.onlinestoremanagementsystem.entity.Cart;
import org.training.onlinestoremanagementsystem.entity.User;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findCartByUser(User user);

    Optional<Cart> findCartByStatusAndUser(String status, User user);

    List<Cart> findAllByUser(User user);
}