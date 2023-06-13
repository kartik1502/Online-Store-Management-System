package org.training.onlinestoremanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantityDto {

    private int productId;

    private int quantity;

    private double price;

    private double totalPrice;
}
