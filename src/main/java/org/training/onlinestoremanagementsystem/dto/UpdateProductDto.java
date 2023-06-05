package org.training.onlinestoremanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDto {

    @Min(1)
    private int quantity;

    @Min(1)
    private double price;
}
