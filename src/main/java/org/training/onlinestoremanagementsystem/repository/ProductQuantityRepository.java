package org.training.onlinestoremanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.onlinestoremanagementsystem.entity.Cart;
import org.training.onlinestoremanagementsystem.entity.ProductQuantity;

import java.util.List;

public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Integer> {

    List<ProductQuantity> findAllByCart(Cart cart);

    List<ProductQuantity> findAllByCartIn(List<Cart> carts);
}
