package org.training.onlinestoremanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartDto {

    private int productId;

    private int quantity;

    public AddToCartDto(int productId) {
        this.productId = productId;
    }
}
