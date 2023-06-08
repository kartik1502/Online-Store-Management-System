package org.training.onlinestoremanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productQuantityId;

    private int productId;

    private int quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cart cart;
}
